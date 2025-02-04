package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class VerifyCodeReq(
    @SerializedName("email")
    var email: String?,
    @SerializedName("verificationCode")
    var verificationCode: String?,
    @SerializedName("userType")
    var userType: String?
)