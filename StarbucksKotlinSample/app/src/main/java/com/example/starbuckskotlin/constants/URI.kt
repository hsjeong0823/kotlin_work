package com.example.starbuckskotlin.constants

import com.example.starbuckskotlin.BuildConfig

class URI {
    companion object {
        /**
         * gradle 에서 설정
         * Server : scheme, authority, domain
         */
        var scheme: String = BuildConfig.SCHEME
        var authority: String = BuildConfig.AUTHORITY
        var authorityPath: String = BuildConfig.AUTHORITY_PATH
        var authorityExternal: String = BuildConfig.AUTHORITY_EXTERNAL
        var authorityExternalPath: String = BuildConfig.AUTHORITY_EXTERNAL_PATH
        var sWebAuthority: String = BuildConfig.WEB_AUTHORITY
        var storeCare: String = BuildConfig.WEB_STORE
        var auth: String = BuildConfig.AUTH_TOKEN
        var authManage: String = BuildConfig.AUTH_MANAGE

        const val MSR_CHECK_INIT = "init.json" // 서버 점검 체크
    }
}