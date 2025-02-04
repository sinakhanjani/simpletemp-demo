package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DentalTempAccountInformation(
    @SerializedName("addressLine1")
    var addressLine1: String?,
    @SerializedName("addressLine2")
    var addressLine2: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("_id")
    var id: String?,
    @SerializedName("mobile")
    var mobile: String?,
    @SerializedName("postalcode")
    var postalcode: String?
) : Parcelable