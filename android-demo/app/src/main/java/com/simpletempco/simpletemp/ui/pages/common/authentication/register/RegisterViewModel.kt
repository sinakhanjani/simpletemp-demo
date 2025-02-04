package com.simpletempco.simpletemp.ui.pages.common.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.RegisterReq
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _registerResult = SingleLiveEvent<Boolean>()
    val registerResult: LiveData<Boolean> get() = _registerResult

    private fun doRegister(
        registerReq: RegisterReq
    ) = viewModelScope.launch {
        onLoading()
        repo.register(registerReq).safeApiCall {
            _registerResult.value = true
        }
    }

    fun register(
        fullName: String,
        email: String,
        password: String,
        rePassword: String,
        userType: String
    ) {
        if (validate(fullName, email, password, rePassword)) {
            if (password != rePassword) {
                onMessage(message = "Password and repeat password does not match")
            } else {
                val data = RegisterReq(fullName, email, password, userType)
                doRegister(data)
            }
        }
    }

}