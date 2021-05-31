package com.example.starbuckskotlin.api

import com.example.starbuckskotlin.constants.URI
import com.example.starbuckskotlin.model.CheckInitRes
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET(URI.MSR_CHECK_INIT)
    fun checkInit(): Call<CheckInitRes>
}