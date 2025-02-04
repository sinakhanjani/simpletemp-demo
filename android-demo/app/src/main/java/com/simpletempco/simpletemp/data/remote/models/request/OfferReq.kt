package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class OfferReq(
    @SerializedName("shiftId")
    var shiftId: String?,
    @SerializedName("preferredPrice")
    var preferredPrice: Int?
)