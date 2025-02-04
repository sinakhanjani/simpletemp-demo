package com.simpletempco.simpletemp.ui.pages.common.support.tickets.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.MessageReq
import com.simpletempco.simpletemp.data.remote.models.response.Message
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _messagesResult = MutableLiveData<List<Message>?>()
    val messagesResult: LiveData<List<Message>?> get() = _messagesResult

    private val _sendMessagesResult = SingleLiveEvent<Message?>()
    val sendMessagesResult: LiveData<Message?> get() = _sendMessagesResult

    fun getMessages(ticketId: String) = viewModelScope.launch {
        onLoading(true)
        repo.message(ticketId).safeApiCall(true) { data ->
            _messagesResult.value = data.data
        }
    }

    private fun doSendMessage(
        ticketId: String,
        messageReq: MessageReq
    ) = viewModelScope.launch {
        onLoading()
        repo.sendMessage(ticketId, messageReq).safeApiCall { data ->
            _sendMessagesResult.value = data.data
        }
    }

    fun sendMessage(
        ticketId: String,
        message: String?
    ) {
        if (message.isNullOrEmpty() || message.trim().isEmpty()) {
            onMessage(message = "The message cannot be empty")
        } else {
            val data = MessageReq(message)
            doSendMessage(ticketId, data)
        }
    }

}