package com.example.starbuckskotlin.api

import android.content.Context
import com.example.starbuckskotlin.account.DeviceInfo
import com.example.starbuckskotlin.constants.Define
import com.example.starbuckskotlin.constants.URI
import com.example.starbuckskotlin.model.BaseResponse
import com.example.starbuckskotlin.model.LoginReq
import com.example.starbuckskotlin.model.LoginRes
import com.example.starbuckskotlin.model.TokenInfo
import com.example.starbuckskotlin.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionManager {
    lateinit var context: Context

    companion object {
        @Volatile
        private var instance : SessionManager? = null

        fun getInstance(): SessionManager = instance ?: synchronized(this) {
            instance ?: SessionManager().also { instance = it }
        }
    }

    fun init(context: Context) {
        this.context = context
    }

    fun requestAuth() {
        val tokenInfo = TokenInfo(grant_type = Define.AUTH_TOKEN_TYPE,
            client_id = Define.AUTH_CLIENT_ID,
            client_secret = Define.AUTH_CLIENT_SECRET,
            refresh_token = "refresh_token",
            deviceId = "deviceId")

        ApiManager.getAuthApi(context).authToken(tokenInfo).run {
            this.enqueue(object : Callback<TokenInfo> {
                override fun onFailure(call: Call<TokenInfo>, t: Throwable) {
                }

                override fun onResponse(call: Call<TokenInfo>, response: Response<TokenInfo>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val loginReq = LoginReq(
                                LoginReq.AppVo(jwt = it.access_token,
                                    deviceId = it.deviceId,
                                    pushId = DeviceInfo.getPushId(context),
                                    osType = DeviceInfo.OS,
                                    osVer = DeviceInfo.getAndroidVersion(),
                                    loginType = "A",
                                    appId = Utility.getNewUUID(context)))
                            requestLogin(loginReq)
                        }
                    }
                }

            })
        }
    }

    private fun requestLogin(loginReq: LoginReq) {

        ApiManager.getApi(context).authJwtLogin(URI.getUrlPath(URI.AUTH_JWT_LOGIN), loginReq).run {
            this.enqueue(object : Callback<LoginRes> {
                override fun onFailure(call: Call<LoginRes>, t: Throwable) {
                }

                override fun onResponse(call: Call<LoginRes>, response: Response<LoginRes>) {
                    if (response.isSuccessful) {
                        val resultCode = response.headers()["resultCode"]
                        val resultMessage = response.headers()["resultMessage"]
                        val requestUrl = call.request().url.toString()
                        val baseResponse = BaseResponse(requestUrl, response.body(), resultCode, resultMessage)
                        if (resultCode == "0109") {
                            requestAuth()
                        }
                    }
                }

            })
        }
    }

    fun verify() {
        val loginReq = LoginReq(
            LoginReq.AppVo(jwt = "access_token",
                deviceId = "deviceId",
                pushId = DeviceInfo.getPushId(context),
                osType = DeviceInfo.OS,
                osVer = DeviceInfo.getAndroidVersion(),
                loginType = "A",
                appId = Utility.getNewUUID(context)))

        requestLogin(loginReq)
    }
}