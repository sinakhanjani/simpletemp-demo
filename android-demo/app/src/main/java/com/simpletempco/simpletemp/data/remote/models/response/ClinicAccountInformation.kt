package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClinicAccountInformation(
    @SerializedName("postalcode")
    var postalcode: String?,
    @SerializedName("addressLine1")
    var addressLine1: String?,
    @SerializedName("addressLine2")
    var addressLine2: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("coordinate")
    var coordinate: Coordinate?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("primaryDentistName")
    var primaryDentistName: String?,
    @SerializedName("primaryDentistCertificationNumber")
    var primaryDentistCertificationNumber: String?,
    @SerializedName("_id")
    var id: String? = null
): Parcelable

@Parcelize
data class Coordinate(
    @SerializedName("latitude")
    var latitude: String?,
    @SerializedName("longitude")
    var longitude: String?
): Parcelable