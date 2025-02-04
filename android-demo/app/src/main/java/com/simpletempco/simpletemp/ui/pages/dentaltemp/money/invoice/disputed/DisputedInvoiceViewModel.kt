package com.simpletempco.simpletemp.ui.pages.dentaltemp.money.invoice.disputed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.DisputedInvoiceIdReq
import com.simpletempco.simpletemp.data.remote.models.response.InvoiceDetail
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisputedInvoiceViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _invoiceResult = MutableLiveData<InvoiceDetail?>()
    val invoiceResult get() = _invoiceResult

    fun getDisputedInvoiceDetails(
        invoiceId: String
    ) = viewModelScope.launch {
        onLoading(true)
        repo.disputedInvoiceDetails(
            invoiceId
        ).safeApiCall(true) { data ->
            _invoiceResult.value = data.data
        }
    }

    fun agreeDisputeInvoice(
        disputedInvoiceId: String?,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.agreeDisputeInvoice(
            DisputedInvoiceIdReq(disputedInvoiceId)
        ).safeApiCall { callback.invoke() }
    }

    fun disagreeDisputeInvoice(
        disputedInvoiceId: String?,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.disagreeDisputeInvoice(DisputedInvoiceIdReq(disputedInvoiceId))
            .safeApiCall { callback.invoke() }
    }

}