package com.example.starbuckskotlin.model

import com.google.gson.annotations.SerializedName

data class VersionReq(@SerializedName("app") var app: AppVo = AppVo()) {
    data class AppVo(@SerializedName("osType") var osType: String? = null,
                     @SerializedName("appVersion") var appVersion: String? = null)
}