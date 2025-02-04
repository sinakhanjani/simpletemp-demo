package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class InvoiceIdReq(
    @SerializedName("invoiceId")
    var invoiceId: String?
)