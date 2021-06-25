package com.example.starbuckskotlin.api

import com.example.starbuckskotlin.constants.URI
import com.example.starbuckskotlin.model.CheckInitRes
import com.example.starbuckskotlin.model.VersionReq
import com.example.starbuckskotlin.model.VersionRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @GET(URI.MSR_CHECK_INIT)
    fun checkInit(): Call<CheckInitRes>

    @POST("{path}")
    fun appVersionCheck(@Path(value = "path", encoded = true) path: String, @Body body: VersionReq): Call<VersionRes>
}