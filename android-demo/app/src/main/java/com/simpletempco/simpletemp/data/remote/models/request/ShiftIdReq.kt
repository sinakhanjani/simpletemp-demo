package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class ShiftIdReq(
    @SerializedName("shiftId")
    var shiftId: String?
)