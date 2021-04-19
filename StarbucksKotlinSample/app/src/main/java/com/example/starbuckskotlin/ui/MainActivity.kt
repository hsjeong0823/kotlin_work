package com.example.starbuckskotlin.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.base.BaseActivity
import com.example.starbuckskotlin.databinding.ActivityMainBinding
import com.example.starbuckskotlin.util.LogUtil

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        fun start(activity: AppCompatActivity) {
            LogUtil.i(MainActivity::class.java.simpleName, "start() called. activity : $activity")
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}