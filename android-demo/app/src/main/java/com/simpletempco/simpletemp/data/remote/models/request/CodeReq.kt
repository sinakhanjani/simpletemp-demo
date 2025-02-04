package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class CodeReq(
    @SerializedName("email")
    var email: String?,
    @SerializedName("userType")
    var userType: String?
)