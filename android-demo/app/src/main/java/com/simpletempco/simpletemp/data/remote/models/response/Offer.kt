package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Offer(
    @SerializedName("_id")
    var id: String?,
    @SerializedName("shift")
    var shift: Shift?,
    @SerializedName("dentalTemp")
    var dentalTemp: DentalTemp?,
    @SerializedName("cost")
    var cost: String?,
    @SerializedName("date")
    var date: String?,
    @SerializedName("preferredPrice")
    var preferredPrice: Int?,
    @SerializedName("state")
    var state: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("shiftDate")
    var shiftDate: String?,
    @SerializedName("expirationDate")
    var expirationDate: String?
) : Parcelable