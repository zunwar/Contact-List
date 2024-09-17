package com.domain.usecase

import com.domain.repository.ListContactsRepository
import javax.inject.Inject

class RefreshListContactsUseCase @Inject constructor(
    private val listContactsRepository: ListContactsRepository
) {
    suspend operator fun invoke() {
        listContactsRepository.syncWithRemote()
    }
}