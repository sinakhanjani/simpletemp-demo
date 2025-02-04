package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.confirmed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.AppConfig.CONFIRMED_SHIFTS_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmedShiftsViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _confirmedShiftsResult = SingleLiveEvent<List<Shift>?>()
    val confirmedShiftsResult get() = _confirmedShiftsResult

    var needUpdateCalendarData = false

    fun getConfirmedShifts(
        page: Int,
        lat: String?,
        long: String?
    ) = viewModelScope.launch {
        if (page == 0) onLoading(true)
        repo.confirmedShifts(CONFIRMED_SHIFTS_QUERY_PER_PAGE, page, lat, long)
            .safeApiCall(page == 0) { data ->
                _confirmedShiftsResult.value = data.data
            }
    }


}