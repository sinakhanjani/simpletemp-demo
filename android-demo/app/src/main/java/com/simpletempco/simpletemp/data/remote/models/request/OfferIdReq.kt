package com.simpletempco.simpletemp.data.remote.models.request


import com.google.gson.annotations.SerializedName

data class OfferIdReq(
    @SerializedName("offerId")
    var offerId: String?
)