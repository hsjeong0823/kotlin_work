package com.example.starbuckskotlin.ui.common

import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.util.LogUtil

class CommonLoadingDialog {
    companion object {
        private val TAG = CommonLoadingDialog::class.java.simpleName
    }

    private var dialog: AppCompatDialog? = null
    private var showCount = 0
    private var handler = Handler(Looper.getMainLooper())

    fun show(activity: AppCompatActivity) {
        if(activity.isFinishing) {
            LogUtil.d(TAG, "show() activity isFinishing")
            return
        }

        if (dialog != null && dialog!!.isShowing) {
            ++showCount
            LogUtil.d(TAG,"show() dialog.isShowing : true, showCount : $showCount, activity : $activity")
            return
        }

        LogUtil.i(TAG, "show() called. dialog create")
        val view = View.inflate(activity, R.layout.common_progress_dialog, null)
        dialog = AppCompatDialog(activity, R.style.LoadingProgressDialog).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setOnKeyListener { innerDialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP && !event.isCanceled) {
                    LogUtil.d(TAG, "setOnKeyListener.onKey() called")
                    innerDialog.dismiss()
                    dialog = null
                    showCount = 0
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            setContentView(view)
            show()
            setOwnerActivity(activity)
        }

        ++showCount
        LogUtil.d(TAG, "show() showCount : $showCount, activity : $activity")
    }

    fun isShowing(): Boolean {
        val result: Boolean = if (null == dialog) {
            false
        } else {
            dialog!!.isShowing
        }
        LogUtil.d(TAG,"isShowing() showCount : $showCount, result : $result, dialog : $dialog")
        return result
    }

    fun dismiss() {
        handler.postDelayed({ internalDismiss() }, 200)
    }

    private fun internalDismiss() {
        if (null == dialog) {
            LogUtil.d(TAG, "dismiss() dialog : null")
            return
        }
        val activity = dialog!!.ownerActivity
        if (null != activity) {
            val isDestroyed = activity.isDestroyed
            val isFinishing = activity.isFinishing
            LogUtil.d(
                TAG,
                "dismiss() isDestroyed : $isDestroyed, isFinishing : $isFinishing, activity : $activity"
            )
            if (isDestroyed || isFinishing) {
                return
            }
        }
        if (!dialog!!.isShowing) {
            return
        }
        --showCount
        LogUtil.d(TAG,"dismiss() showCount : " + showCount + ", activity : " + dialog!!.ownerActivity)
        if (0 >= showCount) {
            
            dialog!!.dismiss()
            LogUtil.i(TAG, "dismiss() called. dialog destroy")
            dialog = null
            showCount = 0
        }
    }

    fun dismissAll() {
        LogUtil.d(TAG, "dismissAll()")
        showCount = 0
        dismiss()
    }
}