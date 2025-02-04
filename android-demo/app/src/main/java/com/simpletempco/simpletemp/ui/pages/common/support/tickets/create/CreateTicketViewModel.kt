package com.simpletempco.simpletemp.ui.pages.common.support.tickets.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.TicketReq
import com.simpletempco.simpletemp.data.remote.models.response.Ticket
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTicketViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _createTicketResult = SingleLiveEvent<Ticket?>()
    val createTicketResult: LiveData<Ticket?> get() = _createTicketResult

    private fun doCreateTicket(
        data: TicketReq
    ) = viewModelScope.launch {
        onLoading()
        repo.createTicket(data).safeApiCall { data ->
            _createTicketResult.value = data.data
        }
    }

    fun createTicket(
        subject: String?,
        message: String?,
        priority: String?,
        department: String?
    ) {
        if (validate(subject, message, priority, department)) {
            val data = TicketReq(subject, message, priority, department)
            doCreateTicket(data)
        }
    }

}