package com.simpletempco.simpletemp.ui.pages.clinic.money.dispute

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.DisputeInvoiceReq
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisputeInvoiceViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    fun disputeInvoice(
        disputeInvoice: DisputeInvoiceReq,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.disputeInvoice(disputeInvoice).safeApiCall { callback.invoke() }
    }

}