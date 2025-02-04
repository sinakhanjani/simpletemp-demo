package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class RequestShiftReq(
    @SerializedName("arrivalTime")
    var arrivalTime: String?,
    @SerializedName("clinicId")
    var clinicId: String?,
    @SerializedName("date")
    var date: String?,
    @SerializedName("endTime")
    var endTime: String?,
    @SerializedName("preferredPrice")
    var preferredPrice: Int?
)