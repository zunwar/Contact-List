package com.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.data.entities.toContactEntity
import com.data.local.database.ContactDao
import com.data.mappers.toDomain
import com.data.remote.network.ContactApiService
import com.data.remote.network.NetworkResult
import com.domain.Constants.TAG
import com.domain.entities.Contact
import com.domain.entities.NameOrPhoneQuery
import com.domain.entities.ShowDataStatus
import com.domain.repository.ListContactsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListContactsRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao,
    private val retrofitService: ContactApiService,
    @ApplicationContext private val appContext: Context
) : ListContactsRepository {

    private val isFirstLaunch: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val showDataStatus: MutableSharedFlow<ShowDataStatus> = MutableSharedFlow(replay = 1)
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "firstLaunch")

    init {
        CoroutineScope(Dispatchers.IO).launch {
            isFirstLaunch()
            dataLoading()
        }
    }

    override suspend fun isFirstLaunch(): StateFlow<Boolean> {
        val IS_FIRST_LAUNCH = booleanPreferencesKey("IS_FIRST_LAUNCH")
        appContext.dataStore
            .edit { firstLaunch ->
                if (firstLaunch[IS_FIRST_LAUNCH] == null) {
                    isFirstLaunch.value = true
                    firstLaunch[IS_FIRST_LAUNCH] = false
                } else {
                    isFirstLaunch.value = false
                }
            }
        return isFirstLaunch
    }

    private suspend fun dataLoading() {
        isFirstLaunch.collect {
            if (isFirstLaunch.value) {
                downloadFromRemoteServer()
            }
            if (!isFirstLaunch.value) {
                showDataStatus.emit(ShowDataStatus.Show)
                delay(60_000)
                downloadFromRemoteServer()
            }
        }
    }

    override suspend fun syncWithRemote() {
        downloadFromRemoteServer()
    }

    private suspend fun downloadFromRemoteServer() {
        Log.d(TAG, "ListContactsRepositoryImpl---downloadFromRemoteServer")
        withContext(Dispatchers.IO) {
            launch {
                val one = async { retrofitService.getContactsFromSourceOne() }
                val two = async { retrofitService.getContactsFromSourceTwo() }
                val tree = async { retrofitService.getContactsFromSourceThree() }
                val responseOne = one.await()
                val responseTwo = two.await()
                val responseThree = tree.await()

                if (responseOne is NetworkResult.Success &&
                    responseTwo is NetworkResult.Success &&
                    responseThree is NetworkResult.Success
                ) {
                    responseOne.data.map { contactDao.insertAll(it.toContactEntity()) }
                    responseTwo.data.map { contactDao.insertAll(it.toContactEntity()) }
                    responseThree.data.map { contactDao.insertAll(it.toContactEntity()) }
                    showDataStatus.emit(ShowDataStatus.Show)
                } else {
                    showDataStatus.emit(ShowDataStatus.Error)
                }
            }
        }
    }

    override suspend fun getListOfContacts(query: NameOrPhoneQuery): Flow<List<Contact>> {
        return contactDao.getContactsWithSearch(query.string).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getShowDataStatus(): SharedFlow<ShowDataStatus> {
        return showDataStatus
    }

}