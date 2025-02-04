package com.simpletempco.simpletemp.ui.pages.clinic.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.response.HomeShiffts
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _shiftsResult = MutableLiveData<Pair<List<HomeShiffts>?, List<HomeShiffts>?>>()
    val shiftsResult get() = _shiftsResult

    init {
        getPendingShifts()
    }

    fun getPendingShifts() = viewModelScope.launch {
        onLoading(true)
        repo.homePendingShifts().safeApiCall(true) { data ->
            getConfirmedShifts(data.data)
        }
    }

    private fun getConfirmedShifts(pending: List<HomeShiffts>?) = viewModelScope.launch {
        repo.homeConfirmedShifts().safeApiCall(true) { data ->
            shiftsResult.value = Pair(pending, data.data)
        }
    }

}