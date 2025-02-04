package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class IntentRes(
    @SerializedName("clientSecret")
    var clientSecret: String?,
    @SerializedName("customer")
    var customer: String?,
    @SerializedName("ephemeralKey")
    var ephemeralKey: String?,
    @SerializedName("publishableKey")
    var publishableKey: String?,
    @SerializedName("setupIntent")
    var setupIntent: String?
)