package com.domain.repository

import com.domain.entities.Contact
import kotlinx.coroutines.flow.Flow

interface InfoContactRepository {

    suspend fun getOneContactById(id: String): Flow<Contact>

}