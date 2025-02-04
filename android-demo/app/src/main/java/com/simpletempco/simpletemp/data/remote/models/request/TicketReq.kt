package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class TicketReq(
    @SerializedName("subject")
    var subject: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("priority")
    var priority: String?,
    @SerializedName("department")
    var department: String?
)