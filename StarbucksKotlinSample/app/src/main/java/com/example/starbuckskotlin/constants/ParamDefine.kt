package com.example.starbuckskotlin.constants

class ParamDefine {
    object Common {
        const val OS_TYPE_IOS = "1"
        const val OS_TYPE_ANDROID = "2"
    }

    object Tag {
        const val TRANSACTION_CHECK_INIT = "TRANSACTION_CHECK_INIT" // 어플리케이션 서버 점검 체크
        const val TRANSACTION_APP_VERSION = "TRANSACTION_APP_VERSION"
        const val TRANSACTION_SESSION_NOTIFY = "TRANSACTION_SESSION_NOTIFY";
        const val TRANSACTION_AUTH = "TRANSACTION_AUTH";       // AUTH 인증
    }
}