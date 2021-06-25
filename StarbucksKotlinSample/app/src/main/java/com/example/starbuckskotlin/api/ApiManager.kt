package com.example.starbuckskotlin.api

import android.content.Context
import android.os.Build
import com.example.starbuckskotlin.BuildConfig
import com.example.starbuckskotlin.account.DeviceInfo
import com.example.starbuckskotlin.constants.URI
import com.example.starbuckskotlin.model.CheckInitRes
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class ApiManager private constructor(var context: Context) {
    companion object {
        private const val ALL_TIMEOUT = 25L

        private const val DEFAULT_MSG_VER = "2"
        private const val MSG_VER_3 = "3"
        private const val MSG_VER_4 = "4"

        @Volatile
        private var apiExternal : ApiInterface? = null

        @Volatile
        private var api : ApiInterface? = null

        fun getExternalApi(context: Context): ApiInterface = apiExternal ?: synchronized(this) {
            apiExternal ?: ApiManager(context).getApiInterface(URI.scheme + "://" + URI.authorityExternal).also { apiExternal = it }
        }

        fun getApi(context: Context): ApiInterface = api ?: synchronized(this) {
            api ?: ApiManager(context).getApiInterface(URI.scheme + "://" + URI.authority).also { api = it }
        }
    }

    private fun getApiInterface(baseUrl: String): ApiInterface {
        /*
         * 로깅 인터셉터 연결
         */
        val httpLogging = HttpLoggingInterceptor()
        httpLogging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val request = chain.request()
                chain.proceed(request.newBuilder().apply {
                    if (!request.url().toString().contains("oauth")) {
                        addHeader("Accept", "application/json")
                        addHeader("osType", "android")
                        addHeader("osVersion", DeviceInfo.getAndroidVersion())
                        addHeader("appVersion", BuildConfig.VERSION_NAME)
                        addHeader("msgVersion", DEFAULT_MSG_VER)
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
            }

            if (BuildConfig.DEBUG) {
                addInterceptor(httpLogging)
            }

            connectTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)

//            val cookieJar: ClearableCookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
//            cookieJar(cookieJar)
        }.build()

        val retrofit = Retrofit.Builder()
            //gson을 이용해 json파싱
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiInterface::class.java)
    }
}