package com.simpletempco.simpletemp.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class HomeShiffts(
    @SerializedName("date")
    val date: String?,
    @SerializedName("shifts")
    val shifts: List<Shift>?
)
