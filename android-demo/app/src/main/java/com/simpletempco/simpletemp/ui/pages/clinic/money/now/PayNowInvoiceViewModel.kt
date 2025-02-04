package com.simpletempco.simpletemp.ui.pages.clinic.money.now

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.response.Invoice
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayNowInvoiceViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _invoiceResult = MutableLiveData<Invoice?>()
    val invoiceResult get() = _invoiceResult

    fun getInvoice(
        invoiceId: String
    ) = viewModelScope.launch {
        onLoading(true)
        repo.getInvoiceDetail(invoiceId).safeApiCall(true) { data ->
            _invoiceResult.value = data.data
        }
    }

    fun payNowInvoice(
        invoiceId: String,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.payNowInvoice(invoiceId).safeApiCall { callback.invoke() }
    }

}