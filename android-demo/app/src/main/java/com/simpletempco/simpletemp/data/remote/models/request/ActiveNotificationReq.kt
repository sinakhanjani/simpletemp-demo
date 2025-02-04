package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class ActiveNotificationReq(
    @SerializedName("activeNotification")
    var activeNotification: Boolean?
)