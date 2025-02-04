package com.simpletempco.simpletemp.util

sealed class UiState {
    object Logout : UiState()
    object PopBackStack : UiState()
    data class Loading(val handleInPage: Boolean = false) : UiState()
    data class ErrorException(val error: Throwable, val handleInPage: Boolean = false) : UiState()
    data class Message(
        val title: String? = null,
        val message: String,
        val positiveButton: String? = null,
        val negativeButton: String? = null,
        val cancelable: Boolean = true,
        val onPositiveButtonClick: (() -> Unit)? = null,
        val handleInPage: Boolean = false
    ) : UiState()
}