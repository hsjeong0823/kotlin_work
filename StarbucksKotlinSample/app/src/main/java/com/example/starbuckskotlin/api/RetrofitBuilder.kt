package com.example.starbuckskotlin.api

import com.example.starbuckskotlin.constants.URI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private var api : ApiInterface

    init {
        val retrofit = Retrofit.Builder()
            //gson을 이용해 json파싱
            .baseUrl(URI.scheme + "://" + URI.authorityExternal)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiInterface::class.java)
    }

    fun getApiInterface(): ApiInterface {
        return api
    }
}