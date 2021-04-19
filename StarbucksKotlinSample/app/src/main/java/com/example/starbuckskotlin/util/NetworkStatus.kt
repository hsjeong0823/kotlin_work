package com.example.starbuckskotlin.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkStatus {
    private val TAG = NetworkStatus::class.java.simpleName

    // 네트워크 (wifi, cellular) 사용 가능 여부 반환
    fun isNetworkConnected(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        LogUtil.e(TAG, "isNetworkConnected() connectivityManager : $connectivityManager")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            LogUtil.d(TAG, "isNetworkConnected() capabilities : $capabilities")
            if (null != capabilities) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = true
                }
            }
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            LogUtil.d(TAG, "isNetworkConnected() activeNetwork : $activeNetwork")
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    result = true
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    result = true
                }
            }
        }
        return result
    }

    // wifi 사용 가능 여부 반환
    fun isWIFIConnected(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        LogUtil.e(TAG, "isWIFIConnected() connectivityManager : $connectivityManager")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            LogUtil.d(TAG, "isWIFIConnected() capabilities : $capabilities")
            if (null != capabilities) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = false
                }
            }
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            LogUtil.d(TAG, "isWIFIConnected() activeNetwork : $activeNetwork")
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    result = true
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    result = false
                }
            }
        }
        return result
    }


    // cellular 사용 가능 여부 반환
    fun isMOBILEConnected(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        LogUtil.e(TAG, "isMOBILEConnected() connectivityManager : $connectivityManager")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            LogUtil.d(TAG, "isMOBILEConnected() capabilities : $capabilities")
            if (null != capabilities) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = false
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = true
                }
            }
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            LogUtil.d(TAG, "isMOBILEConnected() activeNetwork : $activeNetwork")
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    result = false
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    result = true
                }
            }
        }
        return result
    }
}