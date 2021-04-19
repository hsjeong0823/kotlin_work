package com.example.starbuckskotlin.account

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import com.example.starbuckskotlin.util.PreferencesUtil
import com.example.starbuckskotlin.util.Utility

class DeviceInfo {
    companion object {
        private const val DEVICE_ID_KEY = "STARBUCKS_APP_UDID_INFO_ID"
        private const val DEVICE_ID_KEY_NEW = "STARBUCKS_APP_UDID_INFO_ID_NEW"
        private const val DEVICE_PUSH_KEY = "GCMID"
        private const val APP_VERSION = "APP_VERSION"
        private const val USER_AGENT = "USER_AGENT"
        const val OS = "2" //OS 구분자(1=iOS, 2=Android)


        /**
         * Preference UDID를 저장한다
         */
        @JvmStatic
        fun setUDID(ctx: Context, UDID: String?) {
            PreferencesUtil.putString(ctx, DEVICE_ID_KEY, UDID)
        }


        /**
         * Preference UDID를 저장한다
         * 소문자 + 32자리로만 저장됨
         */
        @JvmStatic
        fun setUUIDNew(ctx: Context, UDID: String?) {
            PreferencesUtil.putString(ctx, DEVICE_ID_KEY_NEW, UDID)
        }

        @JvmStatic
        fun getUDIDIfEmptyMake(ctx: Context): String? {
            var UDID: String? = PreferencesUtil.getString(ctx, DEVICE_ID_KEY)
            if (TextUtils.isEmpty(UDID)) {
                UDID = Utility.getUUID(ctx)
                setUDID(ctx, UDID)
            }
            return UDID
        }

        /**
         * Preference에 저장되어있는 UDID를 가져온다
         *
         * @return String UDID
         */
        @JvmStatic
        fun getUDID(ctx: Context): String? {
            return PreferencesUtil.getString(ctx, DEVICE_ID_KEY)
        }


        /**
         * Preference에 저장되어있는 UDID를 가져온다.
         * 소문자 + 32자리로만 저장됨
         *
         * @return String UDID
         */
        @JvmStatic
        fun getUUIDNew(ctx: Context): String? {
            return PreferencesUtil.getString(ctx, DEVICE_ID_KEY_NEW)
        }

        /**
         * Preference Push ID(Token)을 저장한다
         *
         * @param ctx Application Context
         * @param pushID Push Token String
         */
        @JvmStatic
        fun setPushId(ctx: Context, pushID: String?) {
            PreferencesUtil.putString(ctx, DEVICE_PUSH_KEY, pushID)
        }

        /**
         * Preference에 저장되어있는 Push ID(Token)를 가져온다
         *
         * @return String Push Token String
         */
        @JvmStatic
        fun getPushId(ctx: Context): String? {
            return PreferencesUtil.getString(ctx, DEVICE_PUSH_KEY, "0")
        }

        /**
         * Android Release Version을 반환한다
         *
         * @return Build.VERSION.RELEASE
         */
        @JvmStatic
        fun getAndroidVersion(): String {
            return Build.VERSION.RELEASE
        }

        /**
         * 현재 Application Version 을 반환한다.
         */
        @JvmStatic
        fun getApplicationVersionName(context: Context): String? {
            val version: String
            version = try {
                val i = context.packageManager.getPackageInfo(context.packageName, 0)
                i.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                return null
            }
            return version
        }

        /**
         * 현재 Application Version 을 반환한다.
         */
        @JvmStatic
        fun getApplicationVersionCode(context: Context): Int {
            return try {
                val i = context.packageManager.getPackageInfo(context.packageName, 0)
                i.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                // should never happen
                throw RuntimeException("Could not get package name: $e")
            }
        }

        /**
         * 커스텀 유저 에이전트 정보를 반환한다
         *
         * @param context Application Context
         */
        @JvmStatic
        fun getCustomUserAgent(context: Context): String? {
            var customUserAgent: String? = PreferencesUtil.getString(context, USER_AGENT, null)
            if (customUserAgent == null) {
                customUserAgent = createCustomUserAgent(context)
            }
            return customUserAgent
        }

        private fun createCustomUserAgent(context: Context): String? {
            val customUserAgent = "Starbucks_Android/" + getApplicationVersionName(context) + "(Android:" + getAndroidVersion() + ")"
            PreferencesUtil.putString(context, USER_AGENT, customUserAgent)
            return customUserAgent
        }
    }
}