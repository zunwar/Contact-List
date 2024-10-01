package com.presentation.info

import androidx.lifecycle.ViewModel
import com.domain.entities.Contact
import com.domain.usecase.GetOneContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class InfoContactViewModel @Inject constructor(
    private val getOneContactUseCase: GetOneContactUseCase
) : ViewModel() {

    suspend fun getOneContactById(id: String): Flow<Contact> {
        return getOneContactUseCase(id)
    }

}