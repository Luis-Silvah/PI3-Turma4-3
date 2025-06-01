package com.team43.superidpi3.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Criptografia {
    private const val SECRET_KEY = "1234567890123456" // 16 caracteres (128 bits)
    private const val INIT_VECTOR = "abcdef9876543210" // 16 caracteres também

    private val charset = Charsets.UTF_8

    fun encrypt(password: String): String {
        val iv = IvParameterSpec(INIT_VECTOR.toByteArray(charset))
        val skeySpec = SecretKeySpec(SECRET_KEY.toByteArray(charset), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)

        val encrypted = cipher.doFinal(password.toByteArray(charset))
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(encrypted: String): String {
        val iv = IvParameterSpec(INIT_VECTOR.toByteArray(charset))
        val skeySpec = SecretKeySpec(SECRET_KEY.toByteArray(charset), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)

        val decodedBytes = Base64.decode(encrypted, Base64.DEFAULT)
        val original = cipher.doFinal(decodedBytes)
        return String(original, charset)
    }
}