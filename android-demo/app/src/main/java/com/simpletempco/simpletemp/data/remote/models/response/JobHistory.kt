package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class JobHistory(
    @SerializedName("jobActivity")
    var jobActivity: JobActivity?,
    @SerializedName("me")
    var me: Me?,
    @SerializedName("offers")
    var offers: List<Offer>?
)

data class JobActivity(
    @SerializedName("perMonth")
    var perMonth: Int?,
    @SerializedName("sum")
    var sum: Int?
)