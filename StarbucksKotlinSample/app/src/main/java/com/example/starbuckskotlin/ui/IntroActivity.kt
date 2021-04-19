package com.example.starbuckskotlin.ui

import android.os.Bundle
import android.widget.Toast
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.base.BaseActivity
import com.example.starbuckskotlin.util.LogUtil
import com.example.starbuckskotlin.util.NetworkStatus

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
            MainActivity.start(this)
        } else {
            Toast.makeText(this, "onNetworkStatusCheck() isNetworkConnected : $isNetworkConnected", Toast.LENGTH_LONG).show()
        }
        finish()
    }

}