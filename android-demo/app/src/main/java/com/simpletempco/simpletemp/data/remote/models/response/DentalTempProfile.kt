package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DentalTempProfile(
    @SerializedName("accountInformation")
    var accountInformation: DentalTempAccountInformation?,
    @SerializedName("bankInformation")
    var bankInformation: DentalTempBankInformation?,
    @SerializedName("isPendingIdentity")
    var isPendingIdentity: Boolean?,
    @SerializedName("percentage")
    var percentage: Int?,
    @SerializedName("personalInformation")
    var personalInformation: DentaTempPersonalInformation?
) : Parcelable