package com.example.starbuckskotlin.model

import com.google.gson.annotations.SerializedName

data class LoginRes(@SerializedName("app") val app: LoginRes.AppVo) {
    data class AppVo(@SerializedName("userId") val userId: String? = null,               // 사용자 아이디
                     @SerializedName("userIdMask") val userIdMask: String? = null,       // 사용자 아이디 마스킹 (텍스트 뒤 2자리 부분 마스킹 (텍스트가 2자리일 경우, 뒤1자리 마스킹))
                     @SerializedName("userName") val userName: String? = null,        // 사용자 이름
                     @SerializedName("userNameMask") val userNameMask: String,               // 사용자 이름 마스킹
                     @SerializedName("msrFlag") val msrFlag: String,                  // MSR 회원 여부 (Y : MSR회원, N : 웹회원)
                     @SerializedName("userGrade") val userGrade: String? = null,  // 사용자 회원 등급 (00 : Welcome등급, 10 : Green등급, 20 : Gold등급)
                     @SerializedName("nicknameUseYn") val nicknameUseYn: String? = null,  // 사용자 닉네임 사용유무 (Y : 사용, N : 사용안함)
                     @SerializedName("nickname") val nickname: String? = null,  // 사용자 닉네임
                     @SerializedName("passwdUpdateYn") val passwdUpdateYn: String? = null,  // 비밀번호 갱신공지 여부 (Y : 공지가 필요한 경우, N : 공지가 필요없는 경우)
                     @SerializedName("deviceIdUpdateYn") val deviceIdUpdateYn: String? = null,  // 단말 고유번호 업데이트 여부 (Y : 업데이트 된 경우, N : 업데이트 안된 경우)
                     @SerializedName("nonRegCardCount") val nonRegCardCount: String? = null,  // 무기명 카드 개수 (단말 고유번호(deviceId)에 등록된 카드 개수)
                     @SerializedName("nonRewardYn") val nonRewardYn: String? = null,  // 준회원 리워드 이관 대상 여부 (Y : 이관 대상)
                     @SerializedName("nonCouponCnt") val nonCouponCnt: String? = null, // 준회원 이관 대상 쿠폰 개수
                     @SerializedName("nonFreqCnt") val nonFreqCnt: String? = null,// 준회원 이관 대상 프리퀀시 스티커 개수 (합)
                     @SerializedName("nonRwdAmt") val nonRwdAmt: String? = null,// 준회원 리워드 누적 실적 금액
                     @SerializedName("nonRwdRemainAmt") val nonRwdRemainAmt: String? = null)          // 준회원 다음 리워드 쿠폰 발행까지 남은 금액


    /*public static class AppVo {
        // 4.1.17. JWT 로그인 - /auth/jwtLogin.do
        public String userId;           // 사용자 아이디
        public String userIdMask;       // 사용자 아이디 마스킹 (텍스트 뒤 2자리 부분 마스킹 (텍스트가 2자리일 경우, 뒤1자리 마스킹))
        public String userName;         // 사용자 이름
        public String userNameMask;     // 사용자 이름 마스킹
        public String msrFlag;          // MSR 회원 여부 (Y : MSR회원, N : 웹회원)
        public String userGrade;        // 사용자 회원 등급 (00 : Welcome등급, 10 : Green등급, 20 : Gold등급)
        public String nicknameUseYn;    // 사용자 닉네임 사용유무 (Y : 사용, N : 사용안함)
        public String nickname;         // 사용자 닉네임
        public String passwdUpdateYn;   // 비밀번호 갱신공지 여부 (Y : 공지가 필요한 경우, N : 공지가 필요없는 경우)
        public String deviceIdUpdateYn; // 단말 고유번호 업데이트 여부 (Y : 업데이트 된 경우, N : 업데이트 안된 경우)
        public String nonRegCardCount;  // 무기명 카드 개수 (단말 고유번호(deviceId)에 등록된 카드 개수)
        public String nonRewardYn;      // 준회원 리워드 이관 대상 여부 (Y : 이관 대상)
        public String nonCouponCnt;     // 준회원 이관 대상 쿠폰 개수
        public String nonFreqCnt;       // 준회원 이관 대상 프리퀀시 스티커 개수 (합)
        public String nonRwdAmt;        // 준회원 리워드 누적 실적 금액
        public String nonRwdRemainAmt;  // 준회원 다음 리워드 쿠폰 발행까지 남은 금액

        // 4.7.1. 개시요청 - /nonmember/auth/startup.do
        public String userType;                                             // 사용자 구분 (3:비회원, 4:준회원)
        public String vi<NonTermsAgreeListRes.TermsAgree> agreeList;        // 약관 동의 리스트 (준회원일 경우)
    }*/
}