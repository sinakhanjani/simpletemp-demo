package com.simpletempco.simpletemp.data.remote.models.response


import com.google.gson.annotations.SerializedName

data class Version(
    @SerializedName("android")
    var android: DeviceInfo?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("iOS")
    var iOS: DeviceInfo?,
    @SerializedName("userType")
    var userType: String?
)

data class DeviceInfo(
    @SerializedName("appURL")
    var appURL: String?,
    @SerializedName("build")
    var build: Int?,
    @SerializedName("version")
    var version: String?
)