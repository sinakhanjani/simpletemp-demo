package com.simpletempco.simpletemp.data.local

import com.simpletempco.simpletemp.data.local.preference.AppPreference
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LocalRepo @Inject constructor(
    private val pref: AppPreference
) {

    fun isLogin() = pref.isLogin()
    fun setLogin(isLogin: Boolean) = pref.setLogin(isLogin)

    fun getToken() = pref.getToken()
    fun saveToken(token: String?) = pref.saveToken(token)

    fun getUserType() = pref.getUserType()
    fun saveUserType(userType: String?) = pref.saveUserType(userType)

    fun getName() = pref.getName()
    fun saveName(name: String?) = pref.saveName(name)

    fun clearData() = pref.clearData()
}
