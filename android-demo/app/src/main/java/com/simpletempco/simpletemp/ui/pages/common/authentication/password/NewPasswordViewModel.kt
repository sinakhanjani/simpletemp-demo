package com.simpletempco.simpletemp.ui.pages.common.authentication.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.ChangePasswordReq
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _newPasswordResult = SingleLiveEvent<Boolean>()
    val newPasswordResult: LiveData<Boolean> get() = _newPasswordResult

    private fun doResetPassword(
        changePasswordReq: ChangePasswordReq
    ) = viewModelScope.launch {
        onLoading()
        repo.changePassword(changePasswordReq)
            .safeApiCall { data ->
                data.data?.token?.let { repo.saveToken(it) }
                _newPasswordResult.value = true
            }
    }

    fun resetPassword(
        email: String?,
        password: String?,
        confirmPassword: String?,
        passwordToken: String?,
        userType: String?
    ) {
        if (password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()) {
            onMessage(message = "Password and Confirm Password is required")
        } else if (password != confirmPassword) {
            onMessage(message = "Password and confirm password does not match")
        } else {
            val data = ChangePasswordReq(email, password, passwordToken, userType)
            doResetPassword(data)
        }
    }

}