package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class DisputeInvoiceReq(
    @SerializedName("invoiceId")
    var invoiceId: String?,
    @SerializedName("arrivalTime")
    var arrivalTime: String?,
    @SerializedName("endTime")
    var endTime: String?,
    @SerializedName("unpaidBreakTime")
    var unpaidBreakTime: String?
)