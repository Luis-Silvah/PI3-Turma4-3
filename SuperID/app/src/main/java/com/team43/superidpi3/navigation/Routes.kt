package com.team43.superidpi3.navigation

object Routes {
    const val Splash = "splash"
    const val Welcome = "welcome"

    const val Home = "home/{idUsuario}"
    const val SignIn = "signin"
    const val ForgotPassword = "forgot-password"
    const val SignUp = "signup"

    const val Categoria = "categoria/{idUsuario}"
    const val AddCategoria = "addCategoria/{idUsuario}"
    const val AddSenha = "addSenha/{idUsuario}"
    const val Profile = "profile/{idUsuario}"

    const val Qrcode = "qrcode/{idUsuario}"

    fun home(idUsuario: String) = "home/${idUsuario}"
    fun qrcode(idUsuario: String) = "qrcode/$idUsuario"
    fun categoria(idUsuario: String) = "categoria/${idUsuario}"
    fun profile(idUsuario: String) = "profile/${idUsuario}"
    fun addSenha(idUsuario: String) = "addSenha/${idUsuario}"
    fun addCategoria(idUsuario: String) = "addCategoria/$idUsuario"

}
