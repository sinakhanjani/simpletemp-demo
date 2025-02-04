package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class MessageReq(
    @SerializedName("message")
    var message: String?
)