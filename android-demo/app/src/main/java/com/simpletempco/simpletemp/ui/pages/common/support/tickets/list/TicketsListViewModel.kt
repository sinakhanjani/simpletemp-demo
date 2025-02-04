package com.simpletempco.simpletemp.ui.pages.common.support.tickets.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.response.Ticket
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketsListViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _ticketsResult = MutableLiveData<List<Ticket>?>()
    val ticketsResult: LiveData<List<Ticket>?> get() = _ticketsResult

    fun getTickets() = viewModelScope.launch {
        onLoading(true)
        repo.ticket().safeApiCall(true) { data ->
            _ticketsResult.value = data.data
        }
    }

}