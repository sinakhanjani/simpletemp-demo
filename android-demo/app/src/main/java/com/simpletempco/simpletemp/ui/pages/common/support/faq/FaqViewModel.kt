package com.simpletempco.simpletemp.ui.pages.common.support.faq

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.util.AppConfig
import com.simpletempco.simpletemp.util.AppConfig.FAQ_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.NetworkUtils.body
import com.simpletempco.simpletemp.util.SingleLiveEvent
import com.simpletempco.simpletemp.util.SuspendType
import com.simpletempco.simpletemp.util.UiStateFaq
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(
    private val repo: AppRepository
) : ViewModel() {

    private val _uiState = SingleLiveEvent<UiStateFaq>()
    val uiState: LiveData<UiStateFaq> = _uiState

    fun loadFaqData(
        page: Int,
        needLoading: Boolean = true
    ) = viewModelScope.launch {
        if (needLoading) updateUiState(UiStateFaq.Loading)
        repo.faq(
            FAQ_QUERY_PER_PAGE,
            page,
            userType() ?: ""
        ).suspendOnSuccess {
            if (data.success) {
                updateUiState(UiStateFaq.Data(data.data))
            } else {
                onErrorMessage(data.code, data.message)
            }
        }.suspendOnError {
            onError(errorBody)
        }.suspendOnException {
            onExceptionOccurred(exception)
        }
    }

    private fun updateUiState(uiState: UiStateFaq) {
        _uiState.value = uiState
    }

    fun logout() = viewModelScope.launch {
        updateUiState(UiStateFaq.Loading)
        val userType = repo.getUserType()
        if (userType == "clinic") {
            repo.clinicLogout()
        } else {
            repo.dentalTempLogout()
        }.suspendOnSuccess {
            if (data.success) {
                repo.clearData()
                updateUiState(UiStateFaq.LogoutSuccess)
            } else {
                onErrorMessage(data.code, data.message)
            }
        }.suspendOnError {
            onError(errorBody)
        }.suspendOnException {
            onExceptionOccurred(exception)
        }
    }

    private fun onErrorMessage(code: Int?, message: String?) {
        val suspendType = when {
            AppConfig.ADMIN_SUSPEND_CODE == code -> SuspendType.ADMIN_SUSPEND
            AppConfig.SUSPEND_CODES.contains(code) -> SuspendType.SUSPEND
            else -> null
        }

        val positiveButtonText = when (suspendType) {
            SuspendType.ADMIN_SUSPEND -> null
            SuspendType.SUSPEND -> "Logout"
            else -> "Ok"
        }

        onMessage(
            message = message ?: "",
            positiveButtonText = positiveButtonText,
            cancelable = (suspendType != SuspendType.ADMIN_SUSPEND),
            onPositiveButtonClick = {
                if (suspendType == SuspendType.SUSPEND) {
                    updateUiState(UiStateFaq.Logout)
                }
            }
        )
    }

    private fun onMessage(
        title: String? = "Oops!",
        message: String,
        positiveButtonText: String? = "Ok",
        negativeButtonText: String? = null,
        cancelable: Boolean = true,
        onPositiveButtonClick: (() -> Unit)? = null
    ) {
        updateUiState(
            UiStateFaq.Message(
                title,
                message,
                positiveButtonText,
                negativeButtonText,
                cancelable,
                onPositiveButtonClick
            )
        )
    }

    private fun onError(errorBody: ResponseBody?) {
        val error = errorBody?.body()
        onErrorMessage(error?.code, error?.message)
    }

    private fun onExceptionOccurred(throwable: Throwable) {
        updateUiState(UiStateFaq.ErrorException(throwable))
    }

    fun userType() = repo.getUserType()

}