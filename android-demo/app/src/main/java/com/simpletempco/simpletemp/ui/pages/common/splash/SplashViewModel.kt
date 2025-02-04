package com.simpletempco.simpletemp.ui.pages.common.splash

import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    fun isLogin() = repo.isLogin()

    fun userType() = repo.getUserType()

}