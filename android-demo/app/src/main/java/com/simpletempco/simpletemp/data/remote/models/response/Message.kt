package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("sender")
    var sender: String?,
    @SerializedName("ticketId")
    var ticketId: String?
)