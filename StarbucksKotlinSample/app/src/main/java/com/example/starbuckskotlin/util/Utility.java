package com.example.starbuckskotlin.util;

import android.content.Context;
import android.text.TextUtils;

import com.example.starbuckskotlin.account.DeviceInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    private static final String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
    private static final int MAX_RANDOM_SIZE = 32;

    private static final int TOKEN_KEY_LENGTH = 32;    // Token 생성 키 Length
    private static final int TOKEN_OFFSET_LENGTH = 1;  // Token Offset Length

    /**
     * Universally unique identifier(RFC 4122)
     */
    public static String getUUID(Context ctx) {
        // return Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
        return UUID.randomUUID().toString();
    }

    public static String getNewUUID(Context ctx) {
        // return Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
        // 기 저장된 Device ID 추출
        String savedDeviceId = DeviceInfo.getUUIDNew(ctx);
        if (TextUtils.isEmpty(savedDeviceId)) {

            // 최초 마이그레이션
            savedDeviceId = DeviceInfo.getUDID(ctx);
        }

        // 기존 Device ID 체크 확인
        if (!validateDeviceID(savedDeviceId)) {
            String newDeviceId = UUID.randomUUID().toString(); // Device ID 새로 생성
            if (!validateDeviceID(newDeviceId)) {
                // 생성한 Device ID 가 유효하지 않은 경우 Random으로 32자리 재 생성
                newDeviceId = makeRandomUUID(MAX_RANDOM_SIZE);
            }

            String checkedDeviceID = newDeviceId.toLowerCase().replace("-", "");
            DeviceInfo.setUUIDNew(ctx, checkedDeviceID);
            return checkedDeviceID;
        }

        return savedDeviceId.toLowerCase().replace("-", "");
    }

    private static boolean validateDeviceID(String deviceID) {
        if (TextUtils.isEmpty(deviceID) || deviceID.length() != 32 || deviceID.contains("null")) {
            return false;
        }

        return true;
    }

    private static String makeRandomUUID(int maxLength) {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < maxLength) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        String saltStr = salt.toString();
        return saltStr;
    }

    /**
     * [보안취약점개선 PJT] 비회원 개시요청 시 APP ID (DeviceID) 암호화 (TOKEN)
     * UUID(APPID) 를 Token 으로 변환한다.
     *
     * @param context
     * @param osType
     * @return
     */
    public static String makeTokenWithUUID(Context context, String osType) {
        String key = Utility.makeRandomUUID(TOKEN_KEY_LENGTH);
        String uuid = Utility.getNewUUID(context);
        String offset = Utility.makeRandomUUID(TOKEN_OFFSET_LENGTH);

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(osType);       // OS 정보 (1자리 , iOS : 1 / Android : 2)
        tokenBuilder.append(key.length()); // 키 길이 (16 ~ 32 자리)
        tokenBuilder.append(key);          // 키 정보 (키 길이)
        try {
            String encUuid = AES256Cipher.AES_Encode(uuid, key);
            if (encUuid == null) {
                encUuid = "";
            }
            tokenBuilder.append(encUuid);      // 암호화된 AppID (가변)
        } catch (Exception e) {
            tokenBuilder.append(""); // 암호화 Exception 발생 시 공백문자 전달
        }
        tokenBuilder.append(offset);       // Offset (1자리)

        return tokenBuilder.toString();
    }

    /**
     * SHA 256 암호화
     *
     * @param pw
     * @return
     */
    public static String encryptSHA256(String pw) {
        if (TextUtils.isEmpty(pw)) {
            return "";
        }

        // [인증개선] 패스워드 "<",">" 치환 로직 추가 2018.04.04 yabigom
        pw = pw.replaceAll("\0", "");
        pw = pw.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        String strHash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pw.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            strHash = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            strHash = "";
        }
        LogUtil.d(TAG, "strHash = " + strHash);
        return strHash;
    }
}
