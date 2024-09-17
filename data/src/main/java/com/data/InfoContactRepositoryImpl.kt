package com.data

import com.data.local.database.ContactDao
import com.data.mappers.toDomain
import com.domain.entities.Contact
import com.domain.repository.InfoContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InfoContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao
) : InfoContactRepository {

    override suspend fun getOneContactById(id: String): Flow<Contact> {
        return contactDao.getContactById(id).map { it.toDomain() }
    }

}