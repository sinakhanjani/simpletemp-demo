package com.simpletempco.simpletemp.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simpletempco.simpletemp.data.remote.models.response.ApiResult
import okhttp3.ResponseBody
import java.lang.reflect.Type

object NetworkUtils {

    fun ResponseBody.body(): ApiResult<Nothing>? {
        val listType: Type = object : TypeToken<ApiResult<Nothing>>() {}.type
        return try {
            Gson().fromJson(string(), listType)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}