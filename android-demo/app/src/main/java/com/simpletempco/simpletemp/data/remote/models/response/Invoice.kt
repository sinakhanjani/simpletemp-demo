package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Invoice(
    @SerializedName("arrivalTime")
    var arrivalTime: String?,
    @SerializedName("billableTime")
    var billableTime: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("endTime")
    var endTime: String?,
    @SerializedName("factorID")
    var factorID: String?,
    @SerializedName("_id")
    var id: String?,
    @SerializedName("isDisputed")
    var isDisputed: Boolean?,
    @SerializedName("preferredPrice")
    var preferredPrice: Int?,
    @SerializedName("shiftDate")
    var shiftDate: String?,
    @SerializedName("totalPrice")
    var totalPrice: String?,
    @SerializedName("totalTime")
    var totalTime: String?,
    @SerializedName("unpaidBreakTime")
    var unpaidBreakTime: String?,
    @SerializedName("dentalTemp")
    var dentalTemp: DentalTemp?,
    @SerializedName("shift")
    var shift: Shift?
) : Parcelable {
    @IgnoredOnParcel
    var isExpand: Boolean = false
}