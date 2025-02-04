package com.simpletempco.simpletemp.ui.pages.common.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.LoginReq
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _loginResult = SingleLiveEvent<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    private fun doLogin(
        email: String,
        password: String,
        userType: String
    ) {
        val data = LoginReq(email, password, userType)
        if (userType.lowercase() == "clinic") {
            clinicLogin(data)
        } else {
            dentalTempLogin(data)
        }
    }

    private fun dentalTempLogin(
        loginReq: LoginReq
    ) = viewModelScope.launch {
        onLoading()
        repo.dentalTempLogin(loginReq).safeApiCall { data ->
            repo.apply {
                saveToken(data.data!!.token)
                saveUserType(loginReq.userType!!)
                saveName(data.data.fullname?:"")
                setLogin(true)
            }
            _loginResult.value = true
        }
    }

    private fun clinicLogin(
        loginReq: LoginReq
    ) = viewModelScope.launch {
        onLoading()
        repo.clinicLogin(loginReq).safeApiCall { data ->
            repo.apply {
                saveToken(data.data!!.token)
                saveUserType(loginReq.userType!!)
                saveName(data.data.fullname?:"")
                setLogin(true)
            }
            _loginResult.value = true
        }
    }

    fun login(
        email: String,
        password: String,
        userType: String
    ) {
        if (validate(email, password)) {
            doLogin(email, password, userType)
        }
    }

}