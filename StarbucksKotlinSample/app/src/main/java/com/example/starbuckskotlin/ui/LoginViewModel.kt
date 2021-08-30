package com.example.starbuckskotlin.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.starbuckskotlin.account.DeviceInfo
import com.example.starbuckskotlin.api.ApiManager
import com.example.starbuckskotlin.base.BaseNetworkResult
import com.example.starbuckskotlin.base.BaseViewModel
import com.example.starbuckskotlin.constants.Define
import com.example.starbuckskotlin.constants.ParamDefine
import com.example.starbuckskotlin.constants.URI
import com.example.starbuckskotlin.model.*
import com.example.starbuckskotlin.util.Utility

class LoginViewModel(application: Application) : BaseViewModel(application) {
    private val mutableLoginRes: MutableLiveData<LoginRes> = MutableLiveData()

    fun getMutableLoginRes() = mutableLoginRes

    fun requestAuth(id: String, pw: String) {
        val tokenInfo = TokenInfo(grant_type = Define.AUTH_TOKEN_TYPE,
            client_id = Define.AUTH_CLIENT_ID,
            client_secret = Define.AUTH_CLIENT_SECRET,
            username = id,
            password = Utility.encryptSHA256(pw))

        ApiManager.getAuthApi(getApplication()).authToken(tokenInfo).run {
            request(this)
        }
    }


    private fun requestLogin(token: TokenInfo) {
        val loginReq = LoginReq(LoginReq.AppVo(jwt = token.access_token,
            deviceId = token.deviceId,
            pushId = DeviceInfo.getPushId(getApplication()),
            osType = DeviceInfo.OS,
            osVer = DeviceInfo.getAndroidVersion(),
            loginType = "M",
            appId = Utility.getNewUUID(getApplication())))

        ApiManager.getApi(getApplication()).authJwtLogin(URI.getUrlPath(URI.AUTH_JWT_LOGIN), loginReq).run {
            request(this)
        }
    }

    override fun onResponseSuccess(transactionType: String?, baseResponse: BaseResponse?) {
        super.onResponseSuccess(transactionType, baseResponse)
        baseResponse?.let {
            if (transactionType == ParamDefine.Tag.TRANSACTION_AUTH) {
                val tokenInfo = it.response as TokenInfo
                requestLogin(tokenInfo)
            } else if (transactionType == ParamDefine.Tag.TRANSACTION_SESSION_NOTIFY) {
                val loginRes = it.response as LoginRes
                mutableLoginRes.value = loginRes
            }
        }
    }

    override fun onResponseFailure(transactionType: String?, baseResponse: BaseResponse?, resultCode: String?, resultMessage: String?) {
        super.onResponseFailure(transactionType, baseResponse, resultCode, resultMessage)
        getMutableNetworkResult().value = BaseNetworkResult(resultCode, resultMessage)
    }
}