package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class Expenses(
    @SerializedName("totalAmountHygienists")
    var totalAmountHygienists: String?,
    @SerializedName("totalAmountNurses")
    var totalAmountNurses: String?
)