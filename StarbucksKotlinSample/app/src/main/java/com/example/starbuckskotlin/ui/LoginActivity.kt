package com.example.starbuckskotlin.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.base.BaseActivity
import com.example.starbuckskotlin.databinding.ActivityLoginBinding
import com.example.starbuckskotlin.ui.common.CommonPopupDialog
import com.example.starbuckskotlin.util.LogUtil

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    companion object {
        fun start(activity: AppCompatActivity) {
            LogUtil.i(LoginActivity::class.java.simpleName, "start() called. activity : $activity")
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setViewModel(viewModel)

        setObserve()
        binding.loginButton.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when(it.id) {
            R.id.loginButton -> {
                if (validate()) {
                    viewModel.requestAuth(binding.idInputEditText.getText().toString(), binding.pwInputEditText.getText().toString())
                }
            }
        }
    }

    private fun setObserve() {
        viewModel.getMutableLoginRes().observe(this, Observer {
            Toast.makeText(this@LoginActivity, it.app.userId, Toast.LENGTH_LONG).show()
        })
    }

    // 유효성 검사
    private fun validate(): Boolean {
        LogUtil.i(TAG, "validate() called")
        val userIdVal = binding.idInputEditText.getText().toString()
        val userPassVal = binding.pwInputEditText.getText().toString()
        var isUserId = !TextUtils.isEmpty(userIdVal)
        isUserId = isUserId && userIdVal.trim().isNotEmpty()
        var isUserPass = !TextUtils.isEmpty(userPassVal)
        isUserPass = isUserPass && userPassVal.trim().isNotEmpty()
        return if (isUserId) {
            //완료버튼 클릭시 다이얼로그
            if (isUserPass) {
                // 로그인 유효성 검사 성공 시
                true
            } else {
                CommonPopupDialog(
                    message = "비밀번호를 입력해 주세요.",
                    positiveButton = getString(R.string.confirm)
                ).show(this)
                false
            }
        } else {
            CommonPopupDialog(
                message = "아이디를 입력해 주세요.",
                positiveButton = getString(R.string.confirm)
            ).show(this)
            false
        }
    }
}