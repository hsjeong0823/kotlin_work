package com.example.starbuckskotlin.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.starbuckskotlin.ui.common.CommonLoadingDialog
import com.example.starbuckskotlin.ui.common.CommonPopupDialog
import com.example.starbuckskotlin.util.LogUtil

open class BaseActivity : AppCompatActivity() {
    protected val TAG = this.javaClass.simpleName
    private val commonLoadingDialog =
        CommonLoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.i(TAG, "onCreate() called.")
    }

    fun setViewModel(viewModel: BaseViewModel) {
        viewModel.getMutableLoadingResult().observe(this, Observer {
            if (it.showLoading) {
                showLoadingDialog()
            } else{
                dismissLoadingDialog()
            }
        })

        viewModel.getMutableNetworkResult().observe(this, Observer {
            CommonPopupDialog(
                message = it.resultCode,
                subMessage = it.resultMessage,
                positiveButton = "확인"
            ).show(this)
        })
    }

    fun showLoadingDialog() {
        commonLoadingDialog.show(this)
    }

    fun dismissLoadingDialog() {
        commonLoadingDialog.dismiss()
    }
}