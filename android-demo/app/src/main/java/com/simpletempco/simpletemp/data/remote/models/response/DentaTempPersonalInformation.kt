package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DentaTempPersonalInformation(
    @SerializedName("certificationNumber")
    var certificationNumber: String?,
    @SerializedName("dbsCertificationURL")
    var dbsCertificationURL: String?,
    @SerializedName("graduationYear")
    var graduationYear: String?,
    @SerializedName("_id")
    var id: String?,
    @SerializedName("resumeURL")
    var resumeURL: String?,
    @SerializedName("specialties")
    var specialties: List<String?>?
): Parcelable