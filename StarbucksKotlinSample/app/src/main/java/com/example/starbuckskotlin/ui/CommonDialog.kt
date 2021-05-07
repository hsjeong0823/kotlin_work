package com.example.starbuckskotlin.ui

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.databinding.CommonDialogLayoutBinding
import com.example.starbuckskotlin.util.LogUtil

class CommonDialog(private var detectBackKey: Boolean = true,
                   private var isNegative: Boolean = true,
                   private var message: String? = null,
                   private var subMessage: String? = null,
                   private var positiveButton: String? = null,
                   private var negativeButton: String? = null,
                   private var positiveOnClickListener: OnClickListener? = null,
                   private var negativeOnClickListener: OnClickListener? = null,
                   fragmentTag: String? = null): AppCompatDialogFragment() {

    companion object {
        private val TAG = CommonDialog::class.java.simpleName
        private const val EXTRA_FRAGMENT_TAG = "EXTRA_FRAGMENT_TAG" // 특정 Dialog 팝업 중복으로 표시 안되도록 별도 tag 설정
        private const val TAG_DEFAULT_COMMON_DIALOG = "TAG_DEFAULT_COMMON_DIALOG"
        private const val TAG_NETWORK_ERROR_DIALOG = "TAG_NETWORK_ERROR_DIALOG"
    }

    init {
        val args = Bundle()
        if (!fragmentTag.isNullOrEmpty()) {
            args.putString(EXTRA_FRAGMENT_TAG, fragmentTag)
        }
        arguments = args
    }

    interface OnClickListener {
        fun onClick(dialog: DialogInterface?)
    }

    fun show(activity: AppCompatActivity?) {
        activity?.let {
            if (!it.isFinishing) {
                var fragmentTag = TAG_DEFAULT_COMMON_DIALOG
                val arguments = arguments
                if (null != arguments) {
                    fragmentTag = arguments.getString(EXTRA_FRAGMENT_TAG, TAG_DEFAULT_COMMON_DIALOG)
                }

                if (fragmentTag != TAG_DEFAULT_COMMON_DIALOG) {
                    // 공통 다이얼로그 팝업의 디폴트 테그가 아닐 경우
                    val fragment = it.supportFragmentManager.findFragmentByTag(fragmentTag)
                    if (fragment is CommonDialog) {
                        // Fragment 존재할 경우 중복으로 표시 되지 않도록 처리
                        return
                    }
                }
                val ft = it.supportFragmentManager.beginTransaction()
                ft.add(this, fragmentTag)
                ft.commitAllowingStateLoss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CommonDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false

        if (detectBackKey) {
            dialog.setOnKeyListener { dialogInterface: DialogInterface?, keyCode: Int, keyEvent: KeyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                    LogUtil.d(TAG, "onKey() KeyEvent.KEYCODE_BACK && KeyEvent.ACTION_UP")
                    dialog.dismiss()
                    if (isNegative) {
                        if (null != negativeOnClickListener) {
                            negativeOnClickListener!!.onClick(dialog)
                        }
                    } else {
                        if (null != positiveOnClickListener) {
                            positiveOnClickListener!!.onClick(dialog)
                        }
                    }
                    return@setOnKeyListener true
                }
                false
            }
        }

        val window = dialog.window
        if (null != window) {
            // corner 적용되어 보이도록 하기 위해 TRANSPARENT 배경 적용
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        } else {
            LogUtil.e(TAG, "onCreateDialog() window : null")
        }
        val view =  View.inflate(context, R.layout.common_dialog_layout, null)
        val binding: CommonDialogLayoutBinding? = DataBindingUtil.bind(view)
        dialog.setContentView(view)

        setLayout(binding)
        return dialog
    }

    private fun setLayout(binding: CommonDialogLayoutBinding?) {
        binding?.let {
            with(it) {
                if (TextUtils.isEmpty(subMessage)) {
                    messageTextView.text = message
                } else {
                    if (TextUtils.isEmpty(message) && !TextUtils.isEmpty(subMessage)) {
                        // mSubMessage 만 입력된 경우
                        messageTextView.text = subMessage
                        messageTextView.visibility = View.VISIBLE
                        topMessageTextView.visibility = View.GONE
                        subMessageTextView.visibility = View.GONE
                    } else {
                        topMessageTextView.text = message
                        subMessageTextView.text = subMessage
                        messageTextView.visibility = View.GONE
                        topMessageTextView.visibility = View.VISIBLE
                        subMessageTextView.visibility = View.VISIBLE
                    }
                }

                if (TextUtils.isEmpty(positiveButton)) {
                    positiveBtn.visibility = View.GONE
                } else {
                    positiveBtn.text = positiveButton
                    positiveBtn.setOnClickListener {
                        dismiss()
                        if (positiveOnClickListener != null) {
                            positiveOnClickListener!!.onClick(dialog)
                        }
                    }
                }

                if (TextUtils.isEmpty(negativeButton)) {
                    negativeBtn.visibility = View.GONE
                } else {
                    negativeBtn.text = negativeButton
                    negativeBtn.setOnClickListener {
                        dismiss()
                        if (negativeOnClickListener != null) {
                            negativeOnClickListener!!.onClick(dialog)
                        }
                    }
                }
            }
        }
    }
}