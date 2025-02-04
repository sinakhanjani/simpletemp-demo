package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class Money(
    @SerializedName("income")
    var income: Income?,
    @SerializedName("offers")
    var offers: List<Offer>?,
    @SerializedName("me")
    var me: Me?
)

data class Income(
    @SerializedName("monthlyIncome")
    var monthlyIncome: String?,
    @SerializedName("totalIncome")
    var totalIncome: String?
)