package com.domain.repository

import com.domain.entities.Contact
import com.domain.entities.DownloadStatus
import com.domain.entities.NameOrPhoneQuery
import com.domain.entities.ShowDataStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ListContactsRepository {

    suspend fun getListOfContacts(query: NameOrPhoneQuery): Flow<List<Contact>>

    suspend fun getShowDataStatus(): SharedFlow<ShowDataStatus>

    suspend fun isFirstLaunch(): StateFlow<Boolean>

    suspend fun syncWithRemote()

}