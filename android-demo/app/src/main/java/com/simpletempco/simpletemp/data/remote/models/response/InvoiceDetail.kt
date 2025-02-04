package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class InvoiceDetail(
    @SerializedName("clinic")
    var clinic: Clinic?,
    @SerializedName("invoice")
    var invoice: Invoice?,
    @SerializedName("disputeInvoice")
    var disputeInvoice: DisputeInvoice?
)

data class DisputeInvoice(
    @SerializedName("_id")
    var id: String?,
    @SerializedName("arrivalTime")
    var arrivalTime: String?,
    @SerializedName("endTime")
    var endTime: String?,
    @SerializedName("unpaidBreakTime")
    var unpaidBreakTime: String?,
    @SerializedName("preferredPrice")
    var preferredPrice: Int?,
    @SerializedName("factorID")
    var factorID: String?,
    @SerializedName("totalTime")
    var totalTime: String?,
    @SerializedName("billableTime")
    var billableTime: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("totalPrice")
    var totalPrice: String?,
    @SerializedName("shift")
    var shift: Shift?
)