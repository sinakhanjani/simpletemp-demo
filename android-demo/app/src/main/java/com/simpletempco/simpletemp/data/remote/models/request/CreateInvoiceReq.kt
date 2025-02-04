package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class CreateInvoiceReq(
    @SerializedName("arrivalTime")
    var arrivalTime: String?,
    @SerializedName("endTime")
    var endTime: String?,
    @SerializedName("shiftId")
    var shiftId: String?,
    @SerializedName("unpaidBreakTime")
    var unpaidBreakTime: String?
)