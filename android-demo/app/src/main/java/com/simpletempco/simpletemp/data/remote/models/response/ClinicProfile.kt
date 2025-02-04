package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClinicProfile(
    @SerializedName("accountInformation")
    var accountInformation: ClinicAccountInformation?,
    @SerializedName("bankInformation")
    var bankInformation: ClinicBankInformation?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("detailInformation")
    var detailInformation: ClinicDetailInformation?,
    @SerializedName("docURL")
    var docURL: String?,
    @SerializedName("isPendingIdentity")
    var isPendingIdentity: Boolean?,
    @SerializedName("percentage")
    var percentage: Int?
) : Parcelable