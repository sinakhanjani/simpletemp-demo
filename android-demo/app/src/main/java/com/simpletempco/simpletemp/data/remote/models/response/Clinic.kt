package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clinic(
    @SerializedName("activeNotification")
    var activeNotification: Boolean?,
    @SerializedName("authenticationSteps")
    var authenticationSteps: List<String>?,
    @SerializedName("badge")
    var badge: Int?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("fullname")
    var fullname: String?,
    @SerializedName("hasShift")
    var hasShift: Boolean?,
    @SerializedName("_id")
    var id: String?,
    @SerializedName("photoURL")
    var photoURL: String?,
    @SerializedName("profile")
    var profile: ClinicProfile?,
    @SerializedName("token")
    var token: String?,
    @SerializedName("distance")
    var distance: String?,
    @SerializedName("userType")
    var userType: String?
): Parcelable