package com.simpletempco.simpletemp.ui.pages.clinic.money

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.response.Invoices
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.AppConfig.INVOICE_QUERY_PER_PAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoneyViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _invoiceResult = MutableLiveData<Invoices?>()
    val invoiceResult get() = _invoiceResult

    fun getInvoices() = viewModelScope.launch {
        onLoading(true)
        repo.getInvoices(
            limit = INVOICE_QUERY_PER_PAGE, page = 0, isPaid = false
        ).safeApiCall(true) { data ->
            _invoiceResult.value = data.data
        }
    }

}