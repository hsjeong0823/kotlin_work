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

        const val APP_VERSION = "/common/version.do" // 4.1.3. APP버전 조회

        const val AUTH_TOKEN = "oauth/token"                                // 3.1 사용자 토큰 받기, 3.2  사용자 토큰 갱신 - 토큰 방식으로 변경되어 새로운 로그인 URL 생성
        const val AUTH_USER = "/oauth/user"                                 // 3.3 사용자 암호 확인 - User 정보 확인 (ID & PW 로 유저 확인 여부)
        const val AUTH_JWT_LOGIN = "/auth/jwtLogin.do"                      // 4.1.17. JWT 로그인 // 토큰 방식으로 변경되어 새로운 로그인 url 생성
        const val NON_MEMBER_LOGIN_V2 = "/nonmember/auth/startupV2.do"      // 4.7.1. 개시요청

        fun getUrlPath(path: String) = authorityPath + path
    }
}