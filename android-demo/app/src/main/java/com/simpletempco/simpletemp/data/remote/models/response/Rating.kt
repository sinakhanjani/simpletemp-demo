package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("averages")
    var averages: Averages?,
    @SerializedName("rates")
    var rates: List<Rate>?,
    @SerializedName("ratesCount")
    var ratesCount: Int?,
    @SerializedName("star1")
    var star1: Star?,
    @SerializedName("star2")
    var star2: Star?,
    @SerializedName("star3")
    var star3: Star?,
    @SerializedName("star4")
    var star4: Star?,
    @SerializedName("star5")
    var star5: Star?
)

data class Star(
    @SerializedName("avg")
    var avg: Int?,
    @SerializedName("count")
    var count: Int?
)

data class Averages(
    @SerializedName("competencyAndSkills")
    var competencyAndSkills: Double?,
    @SerializedName("professionalism")
    var professionalism: Double?,
    @SerializedName("rate")
    var rate: Double?,
    @SerializedName("timekeeping")
    var timekeeping: Double?
)

data class Rate(
    @SerializedName("clinic")
    var clinic: Clinic?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("rate")
    var rate: Int?
)