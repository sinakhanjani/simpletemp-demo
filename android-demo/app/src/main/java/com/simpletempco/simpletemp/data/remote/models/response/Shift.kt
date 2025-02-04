package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Shift(
    @SerializedName("clinic")
    var clinic: Clinic? = null,
    @SerializedName("dentalTemp")
    var dentalTemp: DentalTemp? = null,
    @SerializedName("offers")
    var offers: List<Offer>? = null,
    @SerializedName("arrivalTime")
    var arrivalTime: String? = null,
    @SerializedName("date")
    var date: String? = null,
    @SerializedName("endTime")
    var endTime: String? = null,
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("preferredPrice")
    var preferredPrice: Int? = null,
    @SerializedName("tag")
    var tag: String? = null,
    @SerializedName("cost")
    var cost: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("unpaidBreakTime")
    var unpaidBreakTime: String? = null,
    @SerializedName("userType")
    var userType: String? = null,
    @SerializedName("countOffers")
    var countOffers: Int? = null
) : Parcelable