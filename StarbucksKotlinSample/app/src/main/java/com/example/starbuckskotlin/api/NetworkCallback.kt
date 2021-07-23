package com.example.starbuckskotlin.api

import com.example.starbuckskotlin.model.BaseResponse

interface NetworkCallback {
    fun onConvertErrorResponse(error: Throwable?, transactionType: String?)      // 네트워크 및 기타 에러일 경우
    fun onResponseSuccess(transactionType: String?, baseResponse: BaseResponse?) // resultCode 0000 이고 json 이 null 이 아닐 경우
    fun onResponseFailure(transactionType: String?, baseResponse: BaseResponse?, resultCode: String?, resultMessage: String?) // resultCode 0000 아닌 경우
}