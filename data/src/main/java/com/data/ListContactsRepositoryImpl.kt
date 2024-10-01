package com.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.data.entities.ContactDto
import com.data.entities.toContactEntity
import com.data.local.database.ContactDao
import com.data.mappers.toDomain
import com.data.remote.network.ContactApiService
import com.data.remote.network.NetworkResult
import com.domain.Constants.TAG
import com.domain.entities.NameOrPhoneQuery
import com.domain.entities.ShowDataStatus
import com.domain.repository.ListContactsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListContactsRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao,
    private val retrofitService: ContactApiService
) : ListContactsRepository {

    private val showDataStatus: MutableSharedFlow<ShowDataStatus> = MutableSharedFlow(replay = 1)
    private val isErrorLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataLoading()
        }
    }

    private suspend fun dataLoading() {
        if (contactDao.isContactDatabaseContainsData()) {
            showDataStatus.emit(ShowDataStatus.Show)
            delay(60_000)
            downloadFromRemoteServer()
        } else {
            downloadFromRemoteServer()
        }
    }

    override suspend fun syncWithRemote() {
        downloadFromRemoteServer()
    }

    private suspend fun downloadFromRemoteServer() {
        Log.d(TAG, "Download from remote started")
        withContext(Dispatchers.IO) {
            val asyncLoad = async {
                launch { loadFromSource(retrofitService.getContactsFromSourceOne()) }
                launch { loadFromSource(retrofitService.getContactsFromSourceTwo()) }
                launch { loadFromSource(retrofitService.getContactsFromSourceThree()) }
            }
            asyncLoad.await()
            Log.d(TAG, "Download from remote finished")
            if (contactDao.isContactDatabaseContainsData() && isErrorLoading.value) {
                showDataStatus.emit(ShowDataStatus.NetworkError)
                isErrorLoading.value = false
            } else if (isErrorLoading.value) {
                showDataStatus.emit(ShowDataStatus.NetworkErrorEmptyDatabase)
            }
        }
    }

    private suspend fun loadFromSource(source: NetworkResult<List<ContactDto>>) {
        if (source is NetworkResult.Success) {
            source.data.map { contactDao.insertAll(it.toContactEntity()) }
            showDataStatus.emit(ShowDataStatus.Show)
            isErrorLoading.value = false
        } else isErrorLoading.value = true
    }

    override suspend fun getShowDataStatus(): SharedFlow<ShowDataStatus> {
        return showDataStatus
    }

    override suspend fun getListOfContacts(query: NameOrPhoneQuery) = Pager(
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            prefetchDistance = 5,
            initialLoadSize = 40
        )
    ) {
        contactDao.getContactsWithSearchPagingSource(query.string)
    }.flow
        .map { pagingData ->
            pagingData.map { it.toDomain() }
        }

}