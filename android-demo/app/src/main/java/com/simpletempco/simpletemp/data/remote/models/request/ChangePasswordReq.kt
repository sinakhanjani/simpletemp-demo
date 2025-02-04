package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class ChangePasswordReq(
    @SerializedName("email")
    var email: String?,
    @SerializedName("password")
    var password: String?,
    @SerializedName("passwordToken")
    var passwordToken: String?,
    @SerializedName("userType")
    var userType: String?
)