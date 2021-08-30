package com.example.starbuckskotlin.api

import com.example.starbuckskotlin.constants.ParamDefine
import com.example.starbuckskotlin.constants.URI
import com.example.starbuckskotlin.model.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET(URI.MSR_CHECK_INIT)
    fun checkInit(@Tag tag: String = ParamDefine.Tag.TRANSACTION_CHECK_INIT): Call<CheckInitRes>

    @POST("{path}")
    fun appVersionCheck(@Path(value = "path", encoded = true) path: String, @Body body: VersionReq, @Tag tag: String = ParamDefine.Tag.TRANSACTION_APP_VERSION): Call<VersionRes>

    @POST(URI.AUTH_TOKEN)
    fun authToken(@Body body: TokenInfo, @Tag tag: String = ParamDefine.Tag.TRANSACTION_AUTH): Call<TokenInfo>

    @POST("{path}")
    fun authJwtLogin(@Path(value = "path", encoded = true) path: String, @Body body: LoginReq, @Tag tag: String = ParamDefine.Tag.TRANSACTION_SESSION_NOTIFY): Call<LoginRes>

    @POST
    @Multipart
    fun getKakaoOcrResult(@Url url: String, @Header("Authorization") key: String, @Part("image\"; filename=\"photo.jpg\"") file: RequestBody) : Call<ResponseBody>
}