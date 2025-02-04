package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DentalTemp(
    @SerializedName("_id")
    var id: String?,
    @SerializedName("activeNotification")
    var activeNotification: Boolean?,
    @SerializedName("authenticationSteps")
    var authenticationSteps: List<String?>?,
    @SerializedName("badge")
    var badge: Int?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("fullname")
    var fullname: String?,
    @SerializedName("photoURL")
    var photoURL: String?,
    @SerializedName("profile")
    var profile: DentalTempProfile?,
    @SerializedName("token")
    var token: String?,
    @SerializedName("passwordToken")
    var passwordToken: String?,
    @SerializedName("userType")
    var userType: String?,
    @SerializedName("rate")
    var rate: Int?
): Parcelable