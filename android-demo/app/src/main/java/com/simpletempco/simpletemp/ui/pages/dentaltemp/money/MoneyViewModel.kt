package com.simpletempco.simpletemp.ui.pages.dentaltemp.money

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.response.Money
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.AppConfig.MONEY_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoneyViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _finishedJobsResult = SingleLiveEvent<Money?>()
    val finishedJobsResult get() = _finishedJobsResult

    fun getJobs(
        page: Int,
        isPaid: Boolean,
        isLoading: Boolean,
        lat: String?,
        long: String?
    ) = viewModelScope.launch {
        if (page == 0 && isLoading){
            onLoading(true)
        } else if (!isLoading) {
            onLoading()
        }
        repo.dentaltempMoney(MONEY_QUERY_PER_PAGE, page, isPaid, lat, long)
            .safeApiCall(page == 0 && isLoading) { data ->
                _finishedJobsResult.value = data.data
            }
    }

}