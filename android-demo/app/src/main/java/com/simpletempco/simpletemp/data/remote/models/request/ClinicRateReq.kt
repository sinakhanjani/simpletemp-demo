package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class ClinicRateReq(
    @SerializedName("shiftId")
    var shiftId: String?,
    @SerializedName("rate")
    var rate: Int?,
    @SerializedName("description")
    var description: String?
)