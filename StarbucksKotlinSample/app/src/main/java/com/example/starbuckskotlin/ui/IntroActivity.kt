package com.example.starbuckskotlin.ui

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.api.RetrofitBuilder
import com.example.starbuckskotlin.base.BaseActivity
import com.example.starbuckskotlin.model.CheckInitRes
import com.example.starbuckskotlin.util.LogUtil
import com.example.starbuckskotlin.util.NetworkStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IntroActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_intro)
        init()
    }

    private fun init() {
        LogUtil.i(TAG, "init() called")
        onNetworkStatusCheck()
    }

    // 네트워크가 연결 가능 여부 확인
    private fun onNetworkStatusCheck() {
        LogUtil.i(TAG, "onNetworkStatusCheck() called")
        val isNetworkConnected: Boolean = NetworkStatus.isNetworkConnected(this)

        LogUtil.d(TAG, "onNetworkStatusCheck() isNetworkConnected : $isNetworkConnected")
        if (isNetworkConnected) {
            RetrofitBuilder.getApiInterface().checkInit().enqueue(object : Callback<CheckInitRes> {
                override fun onResponse(call: Call<CheckInitRes>, response: Response<CheckInitRes>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            LogUtil.d(TAG, "checkInit() resultCode : ${it.resultCode}")
                            CommonDialog(message = "checkInit() resultCode : ${it.resultCode}",
                                subMessage = it.resultMessage,
                                positiveButton = "확인",
                                positiveOnClickListener = object : CommonDialog.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?) {
                                        MainActivity.start(this@IntroActivity)
                                        finish()
                                    }
                                }).show(this@IntroActivity)
                        }
                    }
                }

                override fun onFailure(call: Call<CheckInitRes>, t: Throwable) {
                }
            })
        } else {
            CommonDialog(message = "네트워크 연결 오류",
                subMessage = "네트워크 연결을 확인해 주세요.",
                positiveButton = "확인",
                positiveOnClickListener = object : CommonDialog.OnClickListener {
                    override fun onClick(dialog: DialogInterface?) {
                        finish()
                    }
                }).show(this)
            Toast.makeText(this, "onNetworkStatusCheck() isNetworkConnected : $isNetworkConnected", Toast.LENGTH_LONG).show()
        }
    }

}