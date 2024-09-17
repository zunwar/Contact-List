package com.domain.usecase

import com.domain.entities.Contact
import com.domain.entities.NameOrPhoneQuery
import com.domain.repository.ListContactsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsWithSearchUseCase @Inject constructor(
    private val contactsRepository: ListContactsRepository
) {

    suspend operator fun invoke(query: NameOrPhoneQuery): Flow<List<Contact>> {
        return contactsRepository.getListOfContacts(query)
    }

}