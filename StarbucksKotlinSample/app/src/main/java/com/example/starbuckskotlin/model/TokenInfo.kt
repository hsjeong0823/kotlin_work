package com.example.starbuckskotlin.model

import com.google.gson.annotations.SerializedName

data class TokenInfo(@SerializedName("result_code") var result_code: String? = null,
                     @SerializedName("result_message") var result_message: String? = null,
                     @SerializedName("grant_type") var grant_type: String? = null,
                     @SerializedName("client_id") var client_id: String? = null,
                     @SerializedName("client_secret") var client_secret: String? = null,
                     @SerializedName("username") var username: String? = null,
                     @SerializedName("password") var password: String? = null,
                     @SerializedName("deviceId") var deviceId: String? = null,
                     @SerializedName("access_token") var access_token: String? = null,
                     @SerializedName("token_type") var token_type: String? = null,
                     @SerializedName("refresh_token") var refresh_token: String? = null,
                     @SerializedName("expires_in") var expires_in: String? = null)