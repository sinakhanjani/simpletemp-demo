package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.shifts

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.ClinicRateReq
import com.simpletempco.simpletemp.data.remote.models.request.OfferIdReq
import com.simpletempco.simpletemp.data.remote.models.request.OfferReq
import com.simpletempco.simpletemp.data.remote.models.request.ShiftIdReq
import com.simpletempco.simpletemp.data.remote.models.response.Calendar
import com.simpletempco.simpletemp.data.remote.models.response.HourlyPrices
import com.simpletempco.simpletemp.data.remote.models.response.Offer
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShiftsViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _calendarResult = SingleLiveEvent<List<Calendar>?>()
    val calendarResult get() = _calendarResult

    private val _hourlyPricesResult = SingleLiveEvent<HourlyPrices?>()
    val hourlyPricesResult get() = _hourlyPricesResult

    private val _shiftDetailsResult = SingleLiveEvent<Shift?>()
    val shiftDetailsResult get() = _shiftDetailsResult

    var needUpdateCalendarData = false

    fun getCalendarInfo(
        from: String,
        to: String,
        lat: String?,
        long: String?
    ) = viewModelScope.launch {
        onLoading(true)
        needUpdateCalendarData = false
        repo.shiftCalendar(from, to, lat, long).safeApiCall(true) { data ->
            _calendarResult.value = data.data
        }
    }

    fun getHourlyPrices() = viewModelScope.launch {
        onLoading()
        repo.setting().safeApiCall { data ->
            _hourlyPricesResult.value = data.data
        }
    }

    fun getShiftDetails(
        shiftId: String?,
        lat: String?,
        long: String?
    ) = viewModelScope.launch {
        onLoading(true)
        repo.shift(ShiftIdReq(shiftId), lat, long).safeApiCall(true) { data ->
            _shiftDetailsResult.value = data.data
        }
    }

    fun cancelOffer(
        id: String,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.cancelOffer(id).safeApiCall {
            callback.invoke()
        }
    }

    fun resubmittedOffer(
        id: String,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.resubmittedOffer(OfferIdReq(id)).safeApiCall {
            callback.invoke()
        }
    }

    fun cancelShift(
        shiftId: String,
        callback: (msg: String?) -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.cancelOffer(shiftId).safeApiCall {
            callback.invoke(it.message)
        }
    }

    fun markAsCompleteShift(
        shiftId: String,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.markAsCompleteShift(ShiftIdReq(shiftId)).safeApiCall {
            callback.invoke()
        }
    }

    fun clinicRate(
        shiftId: String,
        rate: Int,
        description: String?,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.clinicRate(ClinicRateReq(shiftId, rate, description)).safeApiCall {
            callback.invoke()
        }
    }

    private fun doSendOffer(
        offer: OfferReq,
        callback: (offer: Offer?) -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.sendOffer(offer).safeApiCall {
            callback.invoke(it.data)
        }
    }

    fun sendOffer(
        id: String,
        preferredPrice: Int?,
        callback: (offer: Offer?) -> Unit
    ) {
        if (preferredPrice == null) {
            onMessage(message = "Please select your hourly rate")
        } else {
            doSendOffer(OfferReq(id, preferredPrice), callback)
        }
    }

    fun isNurseUser() = repo.getUserType()?.lowercase().equals("nurse")

}