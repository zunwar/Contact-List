package com.domain.usecase

import androidx.paging.PagingData
import com.domain.entities.Contact
import com.domain.entities.NameOrPhoneQuery
import com.domain.repository.ListContactsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsWithSearchUseCase @Inject constructor(
    private val contactsRepository: ListContactsRepository
) {
    suspend operator fun invoke(query: NameOrPhoneQuery): Flow<PagingData<Contact>> {
        val pattern = Regex("[a-z]")
        val formattedQuery = if (pattern.containsMatchIn(query.string)) {
            query.string
        } else {
            query.string.filter { it.isDigit() }
        }
        return contactsRepository.getListOfContacts(NameOrPhoneQuery(formattedQuery))
    }

}