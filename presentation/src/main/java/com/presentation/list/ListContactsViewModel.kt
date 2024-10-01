package com.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListContactsViewModel @Inject constructor(
    private val getContactsWithSearchUseCase: GetContactsWithSearchUseCase,
    private val getShowDataUseCase: ShowDataUseCase,
    private val refreshListContactsUseCase: RefreshListContactsUseCase,
) : ViewModel() {

    private val searchQueryStateFlow = MutableStateFlow(NoQuery)

    suspend fun getShowDataStatus(): SharedFlow<ShowDataStatus> {
        return getShowDataUseCase()
    }

    fun refreshList() {
        viewModelScope.launch(Dispatchers.IO) { refreshListContactsUseCase() }
    }

    fun search(query: String) {
        searchQueryStateFlow.value = NameOrPhoneQuery(query)
    }

    fun getSearchQuery(): String {
        return searchQueryStateFlow.value.string
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getContactsList(): Flow<PagingData<Contact>> {
        return searchQueryStateFlow.flatMapLatest {
            getContactsWithSearchUseCase(query = it)
        }.cachedIn(viewModelScope)
    }

}
