package com.example.starbuckskotlin.util

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AES256Cipher {
    private var ivBytes = ByteArray(16)

    @JvmStatic
    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun AES_Encode(str: String, key: String): String? {
        val str1 = str.toByteArray(charset("UTF-8"))
        val var2 = IvParameterSpec(ivBytes)
        val key1 = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
        val var3: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        var3.init(1, key1, var2)
        return Base64.encodeToString(var3.doFinal(str1), Base64.NO_WRAP)
    }

    @JvmStatic
    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun AES_Decode(str: String?, key: String): String? {
        val str1 = Base64.decode(str, Base64.NO_WRAP)
        val var2 = IvParameterSpec(ivBytes)
        val key1 = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
        val var3: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        var3.init(2, key1, var2)
        return String(var3.doFinal(str1), StandardCharsets.UTF_8)
    }
}