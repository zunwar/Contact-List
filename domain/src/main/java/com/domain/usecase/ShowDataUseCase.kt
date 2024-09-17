package com.domain.usecase

import com.domain.entities.ShowDataStatus
import com.domain.repository.ListContactsRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class ShowDataUseCase @Inject constructor(
    private val listContactsRepository: ListContactsRepository
) {

    suspend operator fun invoke(): SharedFlow<ShowDataStatus> =
        listContactsRepository.getShowDataStatus()

}