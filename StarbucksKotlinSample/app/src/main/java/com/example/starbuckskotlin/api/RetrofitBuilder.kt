package com.example.starbuckskotlin.api

import com.example.starbuckskotlin.BuildConfig
import com.example.starbuckskotlin.constants.URI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    private var api : ApiInterface

    init {
        val httpLogging = HttpLoggingInterceptor()
        httpLogging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().apply {
            /*addInterceptor { chain ->
                val request = chain.request()
                chain.proceed(request.newBuilder().apply {
                    if (!request.url.toString().contains("oauth")) {
                        addHeader("Accept", "application/json")
                        addHeader("osType", "android")
                        addHeader("osVersion", DeviceInfo.getAndroidVersion())
                        addHeader("appVersion", BuildConfig.VERSION_NAME)
                        addHeader("msgVersion", ApiManager.DEFAULT_MSG_VER)
                        addHeader("model", Build.MODEL)
                        addHeader("User-Agent", DeviceInfo.getCustomUserAgent(context))
                    } else {
                        addHeader("Content-Type", "application/json")
                        addHeader("User-Agent", DeviceInfo.getCustomUserAgent(context))
                    }

//                    if (mHeaderJSessionId) {
//                        addHeader("jsessionid", AccountInfo.getInstance().getJSessionID())
//                    }
                }.build())
            }*/

            if (BuildConfig.DEBUG) {
                addInterceptor(httpLogging)
            }

            connectTimeout(25L, TimeUnit.SECONDS)
            writeTimeout(25L, TimeUnit.SECONDS)
            readTimeout(25L, TimeUnit.SECONDS)
        }.build()

        val retrofit = Retrofit.Builder()
            //gson을 이용해 json파싱
            .client(okHttpClient)
            .baseUrl(URI.scheme + "://" + URI.authorityExternal)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiInterface::class.java)
    }

    fun getApiInterface(): ApiInterface {
        return api
    }
}