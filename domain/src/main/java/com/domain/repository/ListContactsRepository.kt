package com.domain.repository

import androidx.paging.PagingData
import com.domain.entities.Contact
import com.domain.entities.NameOrPhoneQuery
import com.domain.entities.ShowDataStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ListContactsRepository {

    suspend fun getShowDataStatus(): SharedFlow<ShowDataStatus>

    suspend fun syncWithRemote()

    suspend fun getListOfContacts(query: NameOrPhoneQuery): Flow<PagingData<Contact>>

}