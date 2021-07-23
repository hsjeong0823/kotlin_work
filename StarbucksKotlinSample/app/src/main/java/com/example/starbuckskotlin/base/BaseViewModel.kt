package com.example.starbuckskotlin.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.starbuckskotlin.api.ApiNetworkPresenter
import com.example.starbuckskotlin.api.NetworkCallback
import com.example.starbuckskotlin.model.BaseResponse
import retrofit2.Call

open class BaseViewModel(application: Application) : AndroidViewModel(application), NetworkCallback {
    private val mutableLoadingResult: MutableLiveData<LoadingResult> = MutableLiveData()
    private val mutableNetworkResult: MutableLiveData<BaseNetworkResult> = MutableLiveData()

    fun getMutableLoadingResult() = mutableLoadingResult

    fun getMutableNetworkResult() = mutableNetworkResult

    fun <T> request(call: Call<T>) {
        getMutableLoadingResult().value = LoadingResult(true)
        ApiNetworkPresenter(this).request(call)
    }

    override fun onConvertErrorResponse(error: Throwable?, transactionType: String?) {
        getMutableLoadingResult().value = LoadingResult(false)
        getMutableNetworkResult().value = BaseNetworkResult("", error?.message)
    }

    override fun onResponseSuccess(transactionType: String?, baseResponse: BaseResponse?) {
        getMutableLoadingResult().value = LoadingResult(false)
    }

    override fun onResponseFailure(transactionType: String?, baseResponse: BaseResponse?, resultCode: String?, resultMessage: String?) {
        getMutableLoadingResult().value = LoadingResult(false)
    }
}

open class BaseNetworkResult(var resultCode: String? = null, var resultMessage: String? = null)
data class LoadingResult(var showLoading : Boolean)