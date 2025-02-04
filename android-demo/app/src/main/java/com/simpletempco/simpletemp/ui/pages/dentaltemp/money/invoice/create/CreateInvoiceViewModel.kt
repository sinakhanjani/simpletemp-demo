package com.simpletempco.simpletemp.ui.pages.dentaltemp.money.invoice.create

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.CreateInvoiceReq
import com.simpletempco.simpletemp.data.remote.models.request.ShiftIdReq
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateInvoiceViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _shiftDetailsResult = SingleLiveEvent<Shift?>()
    val shiftDetailsResult get() = _shiftDetailsResult

    fun getShiftDetails(
        shiftId: String?
    ) = viewModelScope.launch {
        onLoading(true)
        repo.shift(ShiftIdReq(shiftId)).safeApiCall(true) { data ->
            _shiftDetailsResult.value = data.data
        }
    }

    fun createInvoice(
        invoiceReq: CreateInvoiceReq,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.createInvoice(invoiceReq).safeApiCall { callback.invoke() }
    }

}