package com.example.starbuckskotlin.model

import com.google.gson.annotations.SerializedName

data class CheckInitRes(@SerializedName("resultCode") val resultCode: String, @SerializedName("resultMessage") val resultMessage: String)