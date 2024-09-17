package com.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.domain.entities.Contact
import com.domain.entities.NameOrPhoneQuery
import com.domain.entities.NoQuery
import com.domain.entities.ShowDataStatus
import com.domain.usecase.ShowDataUseCase
import com.domain.usecase.GetContactsWithSearchUseCase
import com.domain.usecase.RefreshListContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListContactsViewModel @Inject constructor(
    private val getContactsWithSearchUseCase: GetContactsWithSearchUseCase,
    private val getShowDataUseCase: ShowDataUseCase,
    private val refreshListContactsUseCase: RefreshListContactsUseCase
) : ViewModel() {

    private val searchQueryStateFlow = MutableStateFlow(NoQuery)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getContactsList(): LiveData<List<Contact>> {
        return searchQueryStateFlow.flatMapLatest {
            getContactsWithSearchUseCase(query = it)
        }.asLiveData()
    }

    fun getShowDataStatus(): LiveData<ShowDataStatus> = liveData {
        getShowDataUseCase().collect { emit(it) }
    }

    fun refreshList() {
        viewModelScope.launch(Dispatchers.IO) { refreshListContactsUseCase() }
    }


    fun search(query: String) {
        searchQueryStateFlow.value = NameOrPhoneQuery(query)
    }

}
