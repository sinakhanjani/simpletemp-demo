package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class FaqItem(
    @SerializedName("answer")
    var answer: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("_id")
    var id: String?,
    @SerializedName("question")
    var question: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?,
    @SerializedName("userType")
    var userType: String?
)