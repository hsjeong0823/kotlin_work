package com.example.starbuckskotlin.model

import com.google.gson.annotations.SerializedName

data class LoginReq(@SerializedName("app") val app: AppVo) {
    data class AppVo(@SerializedName("jwt") val jwt: String? = null,              // JSON Web Token (AES256 암호화된 값) // access 토큰 auth에서 받은 토큰을 세팅한다
                     @SerializedName("deviceId") val deviceId: String? = null,            // JWT 토큰 암호화 키
                     @SerializedName("pushId") val pushId: String? = null,        // 푸시 아이디
                     @SerializedName("osType") val osType: String,                // 단말 os (1 : IOS, 2 : ANDROID, 9 : ETC)
                     @SerializedName("osVer") val osVer: String,                  // OS Version
                     @SerializedName("loginType") val loginType: String? = null,  // 로그인타입 (M : 사용자입력, A : 자동로그인)
                     @SerializedName("appId") val appId: String? = null)          // APP 고유값
}