package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClinicDetailInformation(
    @SerializedName("managerName")
    var managerName: String?,
    @SerializedName("parking")
    var parking: String?,
    @SerializedName("radiography")
    var radiography: String?,
    @SerializedName("ultrasonic")
    var ultrasonic: String?,
    @SerializedName("charting")
    var charting: String?,
    @SerializedName("software")
    var software: String?,
    @SerializedName("_id")
    var id: String? = null
) : Parcelable