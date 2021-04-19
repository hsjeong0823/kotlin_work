package com.example.starbuckskotlin.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.PowerManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;

import com.example.starbuckskotlin.account.DeviceInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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

    public static String getAppKeyHash(Context context) {
        if (context == null) return null;

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                if (signature != null) {
                    return doFingerprint(signature.toByteArray(), "SHA-256"); // FIndBug FIX MD5 -> SHA256, yabigom 20190308
                }
            }
        } catch (Exception e) {
            LogUtil.e("hash key", "getAppKeyHash() e : " + e);
        }

        return null;
    }

    public static String doFingerprint(byte[] certificateBytes, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(certificateBytes);
        byte[] digest = md.digest();

        String toRet = "";
        for (int i = 0; i < digest.length; i++) {
            if (i != 0) toRet += ":";
            int b = digest[i] & 0xff;
            String hex = Integer.toHexString(b);
            if (hex.length() == 1) toRet += "0";
            toRet += hex;
        }
        return toRet;
    }

    public static boolean isNullOrEmpty(String s) {
        return (s == null) || (s.length() == 0) || (s.toLowerCase().equals("null"));
    }

    public static String getString(String data) {
        return !TextUtils.isEmpty(data) ? data : "";
    }

    /**
     * '5000' 표시를 '5,000 원' 으로 변경하여 리턴
     */
    public static String getWonFormatKo(String money) {
        int won = 0;
        try {
            won = Integer.parseInt(money);
        } catch (Exception e) {
            LogUtil.e(TAG, "getWonFormatKo() Exception : " + e);
        }

        DecimalFormat df = new DecimalFormat("##,###.##");
        // (char)65510은 ￦ 표시를 하기위해서 사용.
        return df.format(won) + " 원";
    }

    private static Pattern pattern = Pattern.compile("^[0-9.-]+$");

    public static String getWonFormatRegex(String money) {
        if (pattern.matcher(money).matches()) {
            int won = 0;
            try {
                won = Integer.parseInt(money);
            } catch (Exception e) {
                LogUtil.e(TAG, "getWonFormatRegex() Exception : " + e);
            }

            DecimalFormat df = new DecimalFormat("##,###.##");
            return df.format(won);
        } else {
            return money;
        }
    }

    public static int parseInt(String value) {
        int val = 0;
        try {
            if (TextUtils.isEmpty(value)) {
                return 0;
            } else {
                val = Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            LogUtil.e(TAG, "parseInt() Exception : " + e);
        }

        return val;
    }

    public static int parseInt(String value, int defaultValue) {
        int val = defaultValue;
        try {
            if (TextUtils.isEmpty(value)) {
                return defaultValue;
            } else {
                val = Integer.parseInt(value);
            }
        } catch (Exception e) {
            LogUtil.d(TAG, "parseInt() Exception : " + e);
        }

        return val;
    }

    /**
     * String 형태의 숫자값을 체크하고 파싱하여 int 형으로 리턴한다.
     */
    public static int parseNumber(String number) {
        int n = 0;

        try {
            if (TextUtils.isEmpty(number)) {
                return n;
            } else if (number.contains(".")) {
                return (int) Double.parseDouble(number);
            } else {
                return Integer.parseInt(number);
            }
        } catch (NumberFormatException e) {
            LogUtil.d(TAG, "parseInt NumberFormatException caught value is =" + number);
        }

        return n;
    }

    public static float parseFloat(String value) {
        float val = 0.0f;
        try {
            val = Float.parseFloat(value);
        } catch (Exception e) {
            LogUtil.e(TAG, "parseFloat() Exception : " + e);
        }

        return val;
    }

    public static double parseDouble(String value) {
        double val = 0.0f;
        try {
            val = Double.parseDouble(value);
        } catch (Exception e) {
            LogUtil.e(TAG, "parseDouble() Exception : " + e);
        }

        return val;
    }

    /**
     * 숫자 -> 문자
     *
     * @return String
     */
    public static String parseString(int value) {
        String val = null;
        try {
            val = String.valueOf(value);
        } catch (Exception e) {
            LogUtil.e(TAG, "parseString() Exception : " + e);
        }

        return val;
    }

    /**
     * Webview 스크린 샷 함수
     * 해당 함수 호출 화면에서 onCreate 전에 WebView.enableSlowWholeDocumentDraw() 를 반드시 호출해야 함. (전체영역 스크린샷가능)
     */
    /*public static boolean snapShotWebview(Activity context, WebView webView, String filename) {
        try {
            File directory = FileUtils.getFile(Environment.getExternalStorageDirectory(), "starbucks");
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    return false;
                }
            }

            File imageFile = FileUtils.getFile(directory, filename);
            if (!imageFile.exists()) {
                if (!imageFile.createNewFile()) {
                    return false;
                }
            }

            webView.setDrawingCacheEnabled(true);
            float scale = webView.getScale();
            int webViewHeight = (int) (webView.getContentHeight() * scale + 0.5);
            Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), webViewHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            webView.draw(canvas);
            webView.setDrawingCacheEnabled(false);

            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.flush();
            fos.close();
            bitmap.recycle();

            //이미지 스캔
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
        } catch (Exception e) {
            return false;
        }

        return true;
    }*/

    /**
     * 서버에서 넘어오는 Boolean "Y" , "N" 값에 따라 리턴 "Y" 라면 true 를 리턴 "N" 이라면 false 를 리턴한다.
     *
     * @param YN "Y" or "N"
     */
    public static boolean isEnable(String YN) {
        return "Y".equalsIgnoreCase(YN);
    }

    /**
     * 스트링 형태로 번호를 넘기면 하이픈(-)을 포함한 폰번호 형태로 리턴한다.
     *
     * @param number ex ) 01012345678
     * @return ex ) 010-1234-5678
     */
    public static String formatPhoneNumber(String number) {
        if (isNullOrEmpty(number)) return number;

        String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
        if (!Pattern.matches(regEx, number)) return number;

        try {
            return number.replaceAll(regEx, "$1-$2-$3");
        } catch (PatternSyntaxException e) {
            LogUtil.e(TAG, "formatPhoneNumber() PatternSyntaxException : " + e);
        }

        return number;
    }

    /**
     * text string 에 Bold 을 추가 한다. ex) textView.setText(StringUtil.boldSpannable(text));
     *
     * @param text Bold 추가할 텍스트
     */
    public static SpannableString boldSpannable(String text) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    // 현재 최상위에 foreground로 앱이 실행중인지 여부
    public static boolean isForeground(@NonNull Context context) {
        LogUtil.i(TAG, "isForeground() called");
        String packageName = context.getPackageName();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        String runPackageName = "";
        List<ActivityManager.RunningAppProcessInfo> runningProcesslist = am.getRunningAppProcesses();
        if (null == runningProcesslist) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesslist) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                runPackageName = processInfo.processName;
                break;
            }
        }

        boolean isForeground = packageName.equals(runPackageName);
        LogUtil.d(TAG, "isForeground() isForeground : " + isForeground);
        return isForeground;
    }

    /**
     * 화면 깨우기
     *
     * @param context 콘텍스트
     * @param tag     함수 호출하는 해당 클래스명
     */
    public static void wakeLockScreenOn(Context context, String tag) {
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.ON_AFTER_RELEASE, tag);
            wl.acquire(10 * 1000);
        } catch (Exception e) {
            LogUtil.e(TAG, "wakeLockScreenOn() Exception : " + e);
        }
    }

    public static boolean getPermissionContact(Context ctx, String key) {
        return PreferencesUtil.getBoolean(ctx, key, false);
    }

    public static void setPermissionContact(Context ctx, String key, boolean granted) {
        PreferencesUtil.putBoolean(ctx, key, granted);
    }

    /**
     * 패키지명으로 해당 앱이 설치 여부를 확인한다.
     *
     * @param packageName 확인할 패키지명
     * @return null(false)이면 미설치, null이 아니면(true) 설치
     */
    public static boolean isAppInstall(Context context, String packageName) {
        // 카카오톡 - com.kakao.talk
        // 라인 - jp.naver.line.android
        Intent startLink = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return null != startLink;
    }

    public static String changeWrapWord(String originText) {
        if (TextUtils.isEmpty(originText)) {
            return "";
        }

        String text = originText.replace(" ", "\u00A0");
        text = text.replace("/", "\u2044");
        text = text.replace("-", "\u2010");
        return text;
    }
}
