package com.simpletempco.simpletemp.ui.pages.dentaltemp.money.invoice.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.InvoiceIdReq
import com.simpletempco.simpletemp.data.remote.models.request.ShiftIdReq
import com.simpletempco.simpletemp.data.remote.models.response.InvoiceDetail
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceDetailsViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _invoiceResult = MutableLiveData<InvoiceDetail?>()
    val invoiceResult get() = _invoiceResult

    fun getInvoiceDetails(
        shiftId: String
    ) = viewModelScope.launch {
        onLoading(true)
        repo.invoiceDetailsByShiftId(shiftId).safeApiCall(true) { data ->
            _invoiceResult.value = data.data
        }
    }

    fun resendInvoice(
        invoiceId: String?,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.resendInvoice(InvoiceIdReq(invoiceId)).safeApiCall { callback.invoke() }
    }

    fun markAsPaidInvoice(
        shiftId: String?,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.markAsPaidInvoice(ShiftIdReq(shiftId)).safeApiCall { callback.invoke() }
    }

}