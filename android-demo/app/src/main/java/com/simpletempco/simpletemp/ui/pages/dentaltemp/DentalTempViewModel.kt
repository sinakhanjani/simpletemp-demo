package com.simpletempco.simpletemp.ui.pages.dentaltemp

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.BuildConfig
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.response.Version
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DentalTempViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _logoutResult = SingleLiveEvent<Boolean>()
    val logoutResult: LiveData<Boolean> get() = _logoutResult

    private val _updateAvailable = SingleLiveEvent<Version>()
    val updateAvailable: LiveData<Version> get() = _updateAvailable

    init {
        checkUpdate()
    }

    private fun checkUpdate() = viewModelScope.launch {
        repo.checkUpdate().suspendOnSuccess {
            if ((data.data?.last()?.android?.build ?: -1) > BuildConfig.VERSION_CODE) {
                _updateAvailable.value = data.data?.last()
            }
        }
    }

    fun logout() = viewModelScope.launch {
        onLoading()
        val userType = repo.getUserType()
        if (userType == "clinic") {
            repo.clinicLogout()
        } else {
            repo.dentalTempLogout()
        }.safeApiCall {
            repo.clearData()
            _logoutResult.value = true
        }
    }

}