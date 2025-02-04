package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class Calendar(
    @SerializedName("date")
    var date: String?,
    @SerializedName("shifts")
    var shifts: List<Shift>?,
    @SerializedName("tag")
    var tag: String?
)