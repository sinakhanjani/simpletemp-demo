package com.simpletempco.simpletemp.ui.pages.clinic.money.all

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.response.Invoices
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.AppConfig.INVOICE_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllInvoiceViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _invoiceResult = SingleLiveEvent<Invoices?>()
    val invoiceResult get() = _invoiceResult

    fun getInvoices(
        needLoading: Boolean,
        page: Int,
        isPaid: Boolean,
        userType: String?
    ) = viewModelScope.launch {
        if (needLoading) {
            onLoading(true)
        } else if (page == 0) {
            onLoading()
        }
        repo.getInvoices(
            INVOICE_QUERY_PER_PAGE,
            page,
            isPaid,
            userType
        ).safeApiCall(needLoading) { data ->
            _invoiceResult.value = data.data
        }
    }

}