package com.example.starbuckskotlin.ui

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.base.BaseActivity
import com.example.starbuckskotlin.databinding.ActivityIntroBinding
import com.example.starbuckskotlin.ui.common.CommonPopupDialog
import com.example.starbuckskotlin.util.LogUtil
import com.example.starbuckskotlin.util.NetworkStatus

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var viewModel: IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        viewModel = ViewModelProvider(this).get(IntroViewModel::class.java)
        setViewModel(viewModel)
        setObserve()

        init()
    }

    private fun setObserve() {
        viewModel.getMutableCheckInitRes().observe(this, Observer {
            if (it.resultCode == "8888") {
                CommonPopupDialog(message = "checkInit() resultCode : ${it.resultCode}",
                    subMessage = it.resultMessage,
                    positiveButton = "확인",
                    positiveOnClickListener = object :
                        CommonPopupDialog.OnClickListener {
                        override fun onClick(dialog: DialogInterface?) {
                            finish()
                        }
                    }).show(this@IntroActivity)
            } else {
                MainActivity.start(this@IntroActivity)
                finish()
            }
        })
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
            viewModel.requestCheckInit()

            /*RetrofitBuilder.getApiInterface().checkInit().enqueue(object : Callback<CheckInitRes> {
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
            })*/
        } else {
            CommonPopupDialog(message = "네트워크 연결 오류",
                subMessage = "네트워크 연결을 확인해 주세요.",
                positiveButton = "확인",
                positiveOnClickListener = object :
                    CommonPopupDialog.OnClickListener {
                    override fun onClick(dialog: DialogInterface?) {
                        finish()
                    }
                }).show(this)
            Toast.makeText(this, "onNetworkStatusCheck() isNetworkConnected : $isNetworkConnected", Toast.LENGTH_LONG).show()
        }
    }

}