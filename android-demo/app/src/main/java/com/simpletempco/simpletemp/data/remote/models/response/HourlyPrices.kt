package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class HourlyPrices(
    @SerializedName("hygienistHourlyPrices")
    var hygienistHourlyPrices: List<Int?>?,
    @SerializedName("hygienistShiftCommission")
    var hygienistShiftCommission: Int?,
    @SerializedName("nurseShiftCommission")
    var nurseShiftCommission: Int?,
    @SerializedName("nusrseHourlyPrices")
    var nurseHourlyPrices: List<Int?>?
)