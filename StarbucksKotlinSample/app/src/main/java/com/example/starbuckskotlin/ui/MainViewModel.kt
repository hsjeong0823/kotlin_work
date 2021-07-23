package com.example.starbuckskotlin.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.starbuckskotlin.account.DeviceInfo
import com.example.starbuckskotlin.api.ApiManager
import com.example.starbuckskotlin.base.BaseNetworkResult
import com.example.starbuckskotlin.base.BaseViewModel
import com.example.starbuckskotlin.constants.ParamDefine
import com.example.starbuckskotlin.constants.URI
import com.example.starbuckskotlin.model.BaseResponse
import com.example.starbuckskotlin.model.VersionReq
import com.example.starbuckskotlin.model.VersionRes

class MainViewModel(application: Application) : BaseViewModel(application) {
    private val mutableVersionRes: MutableLiveData<VersionRes> = MutableLiveData()

    fun getMutableVersionRes() = mutableVersionRes

    fun requestVersion() {
        val versionReq = VersionReq().apply {
            app.osType = ParamDefine.Common.OS_TYPE_ANDROID
            app.appVersion = DeviceInfo.getApplicationVersionName(getApplication())
        }

        ApiManager.getApi(getApplication()).appVersionCheck(URI.getUrlPath(URI.APP_VERSION), versionReq).run {
            request(this)
        }
    }

    override fun onResponseSuccess(transactionType: String?, baseResponse: BaseResponse?) {
        super.onResponseSuccess(transactionType, baseResponse)
        baseResponse?.let {
            val versionRes = it.response as VersionRes
            mutableVersionRes.value = versionRes
        }
    }

    override fun onResponseFailure(transactionType: String?, baseResponse: BaseResponse?, resultCode: String?, resultMessage: String?) {
        super.onResponseFailure(transactionType, baseResponse, resultCode, resultMessage)
        getMutableNetworkResult().value = BaseNetworkResult(resultCode, resultMessage)
    }
}