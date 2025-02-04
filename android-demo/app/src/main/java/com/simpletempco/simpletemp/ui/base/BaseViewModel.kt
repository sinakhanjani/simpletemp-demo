package com.simpletempco.simpletemp.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.simpletempco.simpletemp.data.remote.models.response.ApiResult
import com.simpletempco.simpletemp.util.AppConfig.ADMIN_SUSPEND_CODE
import com.simpletempco.simpletemp.util.AppConfig.SUSPEND_CODES
import com.simpletempco.simpletemp.util.NetworkUtils.body
import com.simpletempco.simpletemp.util.SingleLiveEvent
import com.simpletempco.simpletemp.util.SuspendType.ADMIN_SUSPEND
import com.simpletempco.simpletemp.util.SuspendType.SUSPEND
import com.simpletempco.simpletemp.util.UiState
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import okhttp3.ResponseBody

open class BaseViewModel : ViewModel() {

    private val _uiState = SingleLiveEvent<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private fun updateUiState(uiState: UiState) {
        _uiState.value = uiState
    }

    private fun onError(
        errorBody: ResponseBody?,
        handleInPage: Boolean = false
    ) {
        val error = errorBody?.body()
        onErrorMessage(error?.code, error?.message, handleInPage)
    }

    private fun onErrorMessage(
        code: Int?,
        message: String?,
        handleInPage: Boolean = false
    ) {
        val suspendType = when {
            ADMIN_SUSPEND_CODE == code -> ADMIN_SUSPEND
            SUSPEND_CODES.contains(code) -> SUSPEND
            else -> null
        }

        val positiveButtonText = when (suspendType) {
            ADMIN_SUSPEND -> null
            SUSPEND -> "Logout"
            else -> "Ok"
        }

        onMessage(
            message = message ?: "",
            positiveButtonText = positiveButtonText,
            cancelable = (suspendType != ADMIN_SUSPEND),
            handleInPage = handleInPage,
            onPositiveButtonClick = {
                if (suspendType == SUSPEND) {
                    updateUiState(UiState.Logout)
                }
            }
        )
    }

    private fun onExceptionOccurred(
        throwable: Throwable,
        handleInPage: Boolean
    ) {
        updateUiState(UiState.ErrorException(throwable, handleInPage))
    }

    fun onMessage(
        title: String? = "Oops!",
        message: String,
        positiveButtonText: String? = "Ok",
        negativeButtonText: String? = null,
        cancelable: Boolean = true,
        onPositiveButtonClick: (() -> Unit)? = null,
        handleInPage: Boolean = false
    ) {
        updateUiState(
            UiState.Message(
                title,
                message,
                positiveButtonText,
                negativeButtonText,
                cancelable,
                onPositiveButtonClick,
                handleInPage
            )
        )
    }

    fun onLoading(handleInPage: Boolean = false) {
        updateUiState(UiState.Loading(handleInPage))
    }

    fun popBackStack() {
        updateUiState(UiState.PopBackStack)
    }

    fun validate(vararg fields: String?): Boolean {
        return if (fields.all { field -> !field.isNullOrEmpty() }) {
            true
        } else {
            onMessage(message = "Please fill all fields")
            false
        }
    }

    suspend fun <T> ApiResponse<ApiResult<T>>.safeApiCall(
        handleInPage: Boolean = false,
        call: (ApiResult<T>) -> Unit
    ) {
        suspendOnSuccess {
            if (data.success) {
                call(data)
            } else {
                onErrorMessage(data.code, data.message, handleInPage)
            }
        }.suspendOnError {
            onError(errorBody, handleInPage)
        }.suspendOnException {
            onExceptionOccurred(exception, handleInPage)
        }
    }

}