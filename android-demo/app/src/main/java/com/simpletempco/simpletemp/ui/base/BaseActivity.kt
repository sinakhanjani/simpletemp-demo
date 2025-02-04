package com.simpletempco.simpletemp.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simpletempco.simpletemp.ui.custom.dialogs.LoadingProgressDialog

open class BaseActivity : AppCompatActivity() {

    private var loadingView: LoadingProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init loading view
        loadingView = LoadingProgressDialog(this)
    }

    fun changeTitle(title: String?) {
        supportActionBar?.title = title ?: ""
    }

    open fun navItemSelect(navItemId: Int) {}

    open fun showLoading() {
        if (loadingView?.isShowing == true) return
        loadingView?.apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            show()
        }
    }

    open fun hideLoading() {
        loadingView?.dismiss()
    }

    open fun setNotificationBadge(badge: Int) {}

    open fun hideBottomNavigation() {}
    open fun showBottomNavigation() {}
    open fun logout() {}

}