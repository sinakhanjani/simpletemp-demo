package com.simpletempco.simpletemp.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class DentaltempHome(
    @SerializedName("pendingShifts")
    val pendingShifts: List<Shift>?,
    @SerializedName("upcomingShifts")
    val upcomingShifts: List<Shift>?,
    @SerializedName("bonus")
    val bonus: Bonus?,
)

data class Bonus(
    @SerializedName("bonus")
    val bonus: Int?
)
