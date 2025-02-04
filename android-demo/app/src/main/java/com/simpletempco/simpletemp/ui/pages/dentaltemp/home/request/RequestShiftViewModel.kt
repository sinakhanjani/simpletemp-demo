package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.request

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.RequestShiftReq
import com.simpletempco.simpletemp.data.remote.models.response.Clinic
import com.simpletempco.simpletemp.data.remote.models.response.HourlyPrices
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestShiftViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _hourlyPrices = SingleLiveEvent<HourlyPrices?>()
    val hourlyPrices get() = _hourlyPrices

    private val _clinicList = SingleLiveEvent<List<Clinic>?>()
    val clinicList get() = _clinicList

    private val _clinicSelect = SingleLiveEvent<Clinic>()
    val clinicSelect get() = _clinicSelect

    fun getHourlyPrices() = viewModelScope.launch {
        onLoading()
        repo.setting().safeApiCall { data ->
            _hourlyPrices.value = data.data
        }
    }

    fun getClinicList() = viewModelScope.launch {
        onLoading()
        repo.searchClinic().safeApiCall { data ->
            _clinicList.value = data.data
        }
    }

    private fun doRequestShift(
        reqShift: RequestShiftReq,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.requestShift(reqShift).safeApiCall { callback.invoke() }
    }

    fun requestShift(
        reqShift: RequestShiftReq?,
        callback: () -> Unit
    ) {
        if (reqShift == null) return
        if (validate(reqShift.clinicId, reqShift.preferredPrice?.toString())) {
            doRequestShift(reqShift, callback)
        }
    }

    fun isNurseUser() = repo.getUserType().equals("nurse")

    fun clinicSelected(item: Clinic) {
        _clinicSelect.value = item
    }

}