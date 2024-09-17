package com.presentation.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.domain.entities.Contact
import com.domain.usecase.GetOneContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class InfoContactViewModel @Inject constructor(
    private val getOneContactUseCase: GetOneContactUseCase
) : ViewModel() {

    fun getOneContactById(id: String): LiveData<Contact> = flow {
        emit(getOneContactUseCase(id).first())
    }.asLiveData()

}