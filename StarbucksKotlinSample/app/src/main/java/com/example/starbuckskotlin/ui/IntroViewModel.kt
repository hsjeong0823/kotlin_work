package com.example.starbuckskotlin.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.starbuckskotlin.api.ApiManager
import com.example.starbuckskotlin.base.BaseNetworkResult
import com.example.starbuckskotlin.base.BaseViewModel
import com.example.starbuckskotlin.constants.ParamDefine
import com.example.starbuckskotlin.model.BaseResponse
import com.example.starbuckskotlin.model.CheckInitRes

class IntroViewModel(application: Application) : BaseViewModel(application) {
    private val mutableCheckInitRes: MutableLiveData<CheckInitRes> = MutableLiveData()

    fun getMutableCheckInitRes() = mutableCheckInitRes

    fun requestCheckInit() {
        ApiManager.getApi(getApplication()).checkInit().run {
            request(this)
        }
    }

    override fun onResponseSuccess(transactionType: String?, baseResponse: BaseResponse?) {
        super.onResponseSuccess(transactionType, baseResponse)
        baseResponse?.let {
            if (transactionType == ParamDefine.Tag.TRANSACTION_CHECK_INIT) {
                val checkInitRes = it.response as CheckInitRes
                getMutableCheckInitRes().value = checkInitRes
            }
        }
    }

    override fun onResponseFailure(transactionType: String?, baseResponse: BaseResponse?, resultCode: String?, resultMessage: String?) {
        super.onResponseFailure(transactionType, baseResponse, resultCode, resultMessage)
        getMutableNetworkResult().value = BaseNetworkResult(resultCode, resultMessage)
    }
}