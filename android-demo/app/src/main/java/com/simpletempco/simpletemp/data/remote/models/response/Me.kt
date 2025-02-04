package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class Me(
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("profile")
    var profile: DentalTempProfile?
)