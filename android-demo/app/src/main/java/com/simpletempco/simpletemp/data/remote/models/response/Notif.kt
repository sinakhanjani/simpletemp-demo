package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class Notif(
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("data")
    var data: Data?,
    @SerializedName("_id")
    var id: String?,
    @SerializedName("isRead")
    var isRead: Boolean?,
    @SerializedName("notification")
    var notification: Notification?
)

data class Data(
    @SerializedName("meta")
    var meta: Meta?
)

data class Meta(
    @SerializedName("type")
    var type: String?,
    @SerializedName("_id")
    var id: String?,
    @SerializedName("ref")
    var ref: String?
)

data class Notification(
    @SerializedName("body")
    var body: String?,
    @SerializedName("title")
    var title: String?
)