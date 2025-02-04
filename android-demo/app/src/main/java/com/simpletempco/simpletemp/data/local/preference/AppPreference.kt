package com.simpletempco.simpletemp.data.local.preference

import android.content.SharedPreferences
import javax.inject.Inject

const val KEY_NAME = "KEY_NAME"
const val KEY_LOGIN = "KEY_LOGIN"
const val KEY_TOKEN = "KEY_TOKEN"
const val KEY_USER_TYPE = "KEY_USER_TYPE"

class AppPreference @Inject constructor(
    private val pref: SharedPreferences
) {

    fun isLogin() = pref.getBoolean(KEY_LOGIN, false)
    fun setLogin(isLogin: Boolean) {
        pref.edit().putBoolean(KEY_LOGIN, isLogin).apply()
    }

    fun getToken() = pref.getString(KEY_TOKEN, "")
    fun saveToken(token: String?) {
        pref.edit().putString(KEY_TOKEN, "Bearer $token").apply()
    }

    fun getUserType() = pref.getString(KEY_USER_TYPE, "")
    fun saveUserType(userType: String?) {
        pref.edit().putString(KEY_USER_TYPE, userType).apply()
    }

    fun getName() = pref.getString(KEY_NAME, "")
    fun saveName(name: String?) {
        pref.edit().putString(KEY_NAME, name).apply()
    }

    fun clearData() {
        pref.edit().clear().apply()
    }

}