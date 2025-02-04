package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class FcmTokenReq(
    @SerializedName("fcmToken")
    var fcmToken: String?
)