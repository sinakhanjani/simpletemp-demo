package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class ReviewReq(
    @SerializedName("competencyAndSkills")
    var competencyAndSkills: Int?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("professionalism")
    var professionalism: Int?,
    @SerializedName("rate")
    var rate: Int?,
    @SerializedName("shiftId")
    var shiftId: String?,
    @SerializedName("timekeeping")
    var timekeeping: Int?
)