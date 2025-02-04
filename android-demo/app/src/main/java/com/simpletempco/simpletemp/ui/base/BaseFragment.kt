package com.simpletempco.simpletemp.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.ui.custom.views.NonDataLayout
import com.simpletempco.simpletemp.util.ContextUtils.isInternetConnected
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.UiState
import okio.IOException

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    abstract val viewModel: VM

    private var nonDataLayout: NonDataLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

        handleState()
    }

    open fun initNonData(
        nonDataLayout: NonDataLayout?,
        retryFunc: () -> Unit
    ) {
        this.nonDataLayout = nonDataLayout
        this.nonDataLayout?.onRetry(retryFunc)
    }

    fun loadingMode() {
        nonDataLayout?.status(NonDataLayout.Status.LOADING)
    }

    private fun handleState() {

        viewModel.uiState.observe(viewLifecycleOwner) { actionState ->
            when (actionState) {
                is UiState.Loading -> showLoading(actionState.handleInPage)
                is UiState.ErrorException -> {
                    hideLoading(actionState.handleInPage)
                    showException(actionState.error, actionState.handleInPage)
                }
                is UiState.Message -> {
                    hideLoading(actionState.handleInPage)
                    if (actionState.handleInPage) nonDataLayout?.status(NonDataLayout.Status.EMPTY)
                    actionState.apply {
                        context?.showMessageDialog(
                            title = title,
                            message = message,
                            positiveButtonText = positiveButton,
                            negativeButtonText = negativeButton,
                            cancelable = cancelable,
                            onPositiveButtonClick = onPositiveButtonClick
                        )
                    }
                }
                UiState.Logout -> {
                    hideLoading()
                    (activity as BaseActivity).logout()
                }
                UiState.PopBackStack -> {
                    hideLoading()
                    popBackStack()
                }
            }
        }
    }

    private fun showException(
        throwable: Throwable,
        handleInPage: Boolean
    ) {
        if (throwable is IOException && context?.isInternetConnected() == false) {
            if (handleInPage) {
                nonDataLayout?.status(NonDataLayout.Status.ERROR)
            } else {
                context?.showMessageDialog(message = getString(R.string.no_internet_connection))
            }
        } else {
            context?.showMessageDialog(
                message = throwable.message ?: "",
                cancelable = false
            )
        }
    }

    fun hideBottomNavigation() {
        (activity as BaseActivity).hideBottomNavigation()
    }

    fun showBottomNavigation() {
        (activity as BaseActivity).showBottomNavigation()
    }

    private fun showLoading(handleInPage: Boolean) {
        if (handleInPage) {
            nonDataLayout?.status(NonDataLayout.Status.LOADING)
        } else {
            (requireActivity() as BaseActivity).showLoading()
        }
    }

    fun hideLoading(handleInPage: Boolean = false) {
        if (handleInPage) {
            nonDataLayout?.status(NonDataLayout.Status.DATA)
        } else {
            (requireActivity() as BaseActivity).hideLoading()
        }
    }

    fun popBackStack() {
        findNavController().popBackStack()
    }

    fun changeTitle(title: String?) {
        (activity as BaseActivity).changeTitle(title)
    }

    fun setNotificationBadge(badge: Int) {
        (requireActivity() as BaseActivity).setNotificationBadge(badge)
    }

    fun navItemSelect(navItemId: Int) {
        (requireActivity() as BaseActivity).navItemSelect(navItemId)
    }

}