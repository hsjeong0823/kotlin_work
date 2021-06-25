package com.example.starbuckskotlin.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.account.DeviceInfo
import com.example.starbuckskotlin.api.ApiManager
import com.example.starbuckskotlin.base.BaseActivity
import com.example.starbuckskotlin.constants.ParamDefine
import com.example.starbuckskotlin.constants.URI
import com.example.starbuckskotlin.databinding.ActivityMainBinding
import com.example.starbuckskotlin.model.VersionReq
import com.example.starbuckskotlin.model.VersionRes
import com.example.starbuckskotlin.util.LogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val versionReq = VersionReq(VersionReq.AppVo(ParamDefine.Common.OS_TYPE_ANDROID, DeviceInfo.getApplicationVersionName(this)))
        ApiManager.getApi(this).appVersionCheck(URI.getUrlPath(URI.APP_VERSION), versionReq).enqueue(object : Callback<VersionRes>{
            override fun onResponse(call: Call<VersionRes>, response: Response<VersionRes>) {
                if (response.isSuccessful) {
                    val versionRes = response.body()
                    Toast.makeText(this@MainActivity, versionRes!!.app.lastestAppVer, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<VersionRes>, t: Throwable) {
            }
        })
    }
}