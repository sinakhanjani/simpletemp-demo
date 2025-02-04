package com.simpletempco.simpletemp.data.remote.models.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClinicBankInformation(
    @SerializedName("intents")
    var intents: List<ClinicIntent?>?
) : Parcelable

@Parcelize
data class ClinicIntent(
    @SerializedName("card")
    var card: Card?,
    @SerializedName("_id")
    var id: String?,
    @SerializedName("isDefault")
    var isDefault: Boolean?,
    @SerializedName("paymentMethodId")
    var paymentMethodId: String?,
    @SerializedName("setupIntentId")
    var setupIntentId: String?,
    @SerializedName("status")
    var status: String?
) : Parcelable

@Parcelize
data class Card(
    @SerializedName("brand")
    var brand: String?,
    @SerializedName("expMonth")
    var expMonth: String?,
    @SerializedName("expYear")
    var expYear: String?,
    @SerializedName("last4")
    var last4: String?
) : Parcelable