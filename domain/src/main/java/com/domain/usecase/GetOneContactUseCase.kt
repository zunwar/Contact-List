package com.domain.usecase

import com.domain.entities.Contact
import com.domain.repository.InfoContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOneContactUseCase @Inject constructor(
    private val infoContactRepository: InfoContactRepository
) {

    suspend operator fun invoke(id: String): Flow<Contact> {
        return infoContactRepository.getOneContactById(id)
    }

}