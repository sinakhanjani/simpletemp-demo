package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("department")
    var department: String?,
    @SerializedName("_id")
    var id: String?,
    @SerializedName("priority")
    var priority: String?,
    @SerializedName("state")
    var state: String?,
    @SerializedName("subject")
    var subject: String?,
    @SerializedName("ticketID")
    var ticketID: Int?,
    @SerializedName("updatedAt")
    var updatedAt: String?,
    @SerializedName("user")
    var user: String?
)