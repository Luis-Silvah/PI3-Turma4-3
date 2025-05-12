package com.team43.superidpi3.domain

class Usuario {
    var nome: String = ""
    var email: String = ""
    var uid: String = ""
    var imei: String = ""
    var emailVerificado: Boolean = false

    fun set(nome: String, email: String, uid: String, imei: String) {
        nome
        email
        uid
        imei
    }

    fun get(key: String): String? {
        return when (key.lowercase()) {
            "nome" -> nome
            "email" -> email
            "uid" -> uid
            "imei" -> imei
            else -> ""
        }
    }
}