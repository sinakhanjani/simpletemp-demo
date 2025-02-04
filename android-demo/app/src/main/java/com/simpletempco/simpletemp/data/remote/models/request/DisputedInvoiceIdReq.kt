package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class DisputedInvoiceIdReq(
    @SerializedName("disputedInvoiceId")
    var disputedInvoiceId: String?
)