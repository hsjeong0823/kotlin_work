package com.example.starbuckskotlin.model

import com.google.gson.annotations.SerializedName

data class VersionRes(@SerializedName("app") var app: AppVo) {
    data class AppVo(@SerializedName("lastestAppVer") var lastestAppVer: String,
                     @SerializedName("updateForceYn") var updateForceYn: String)
}