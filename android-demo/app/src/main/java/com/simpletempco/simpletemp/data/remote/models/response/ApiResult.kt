package com.simpletempco.simpletemp.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class ApiResult<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: T?
)
