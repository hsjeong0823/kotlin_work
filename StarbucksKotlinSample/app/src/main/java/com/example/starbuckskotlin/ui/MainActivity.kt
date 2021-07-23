package com.example.starbuckskotlin.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.base.BaseActivity
import com.example.starbuckskotlin.databinding.ActivityMainBinding
import com.example.starbuckskotlin.util.LogUtil

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

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
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setViewModel(viewModel)
        setObserve()

        viewModel.requestVersion()
        /*ApiManager.getApi(this).appVersionCheck(URI.getUrlPath(URI.APP_VERSION), versionReq).enqueue(object : Callback<VersionRes>{
            override fun onResponse(call: Call<VersionRes>, response: Response<VersionRes>) {
                if (response.isSuccessful) {
                    val versionRes = response.body()
                    Toast.makeText(this@MainActivity, versionRes!!.app.lastestAppVer, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<VersionRes>, t: Throwable) {
            }
        })*/

        binding.ocrTest.setOnClickListener {
            val intent = Intent(this, OcrTestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setObserve() {
        viewModel.getMutableVersionRes().observe(this, Observer {
            Toast.makeText(this@MainActivity, it.app.lastestAppVer, Toast.LENGTH_LONG).show()
        })
    }

}