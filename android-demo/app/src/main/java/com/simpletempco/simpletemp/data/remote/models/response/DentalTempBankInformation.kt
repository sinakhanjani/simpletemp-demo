package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DentalTempBankInformation(
    @SerializedName("accountNumber")
    var accountNumber: String?,
    @SerializedName("bankName")
    var bankName: String?,
    @SerializedName("beneficiaryName")
    var beneficiaryName: String?,
    @SerializedName("bic")
    var bic: String?,
    @SerializedName("iban")
    var iban: String?,
    @SerializedName("sortCode")
    var sortCode: String?,
    @SerializedName("_id")
    var id: String? = null,
) : Parcelable