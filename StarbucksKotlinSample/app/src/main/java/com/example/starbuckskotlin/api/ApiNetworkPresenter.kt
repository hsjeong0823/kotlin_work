package com.example.starbuckskotlin.api

import com.example.starbuckskotlin.model.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiNetworkPresenter constructor(var networkCallBack: NetworkCallback? = null)  {
    fun <T> request(call: Call<T>) {
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                networkCallBack?.onConvertErrorResponse(t, call.request().tag(String::class.java))
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val resultCode = response.headers()["resultCode"]
                    val resultMessage = response.headers()["resultMessage"]
                    val requestUrl = call.request().url.toString()
                    val baseResponse = BaseResponse(requestUrl, response.body(), resultCode, resultMessage)
                    if (resultCode == null || resultCode == "0000") {
                        networkCallBack?.onResponseSuccess(call.request().tag(String::class.java), baseResponse)
                    } else {
                        if (resultCode == "0001") {
                            SessionManager.getInstance().verify()
                        } else {
                            networkCallBack?.onResponseFailure(call.request().tag(String::class.java), baseResponse, resultCode, resultMessage)
                        }
                    }
                } else {
                    networkCallBack?.onConvertErrorResponse(null, call.request().tag(String::class.java))
                }
            }
        })
    }
}
