package com.simpletempco.simpletemp.util

import com.simpletempco.simpletemp.data.remote.models.response.FaqItem

sealed class UiStateFaq {
    object Logout : UiStateFaq()
    object Loading : UiStateFaq()
    object LogoutSuccess : UiStateFaq()
    data class Data(val data: List<FaqItem>?) : UiStateFaq()
    data class ErrorException(val error: Throwable, val handleInPage: Boolean = false) : UiStateFaq()
    data class Message(
        val title: String? = null,
        val message: String,
        val positiveButton: String? = null,
        val negativeButton: String? = null,
        val cancelable: Boolean = true,
        val onPositiveButtonClick: (() -> Unit)? = null,
        val handleInPage: Boolean = false
    ) : UiStateFaq()
}
