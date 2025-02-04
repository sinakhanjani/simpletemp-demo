package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class Invoices(
    @SerializedName("invoices")
    var invoices: List<Invoice>?,
    @SerializedName("expenses")
    var expenses: Expenses?
)