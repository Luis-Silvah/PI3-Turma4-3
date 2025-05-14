package com.team43.superidpi3.navigation

object Routes {
    const val Splash = "splash"
    const val Welcome = "welcome"

    const val Home = "home/{idUsuario}"
    const val SignIn = "signin"
    const val ForgotPassword = "forgot-password"
    const val SignUp = "signup"

    const val Categoria = "categoria/{idUsuario}"

    const val Qrcode = "qrcode"
    const val Profile = "profile/{idUsuario}"

    fun home(idUsuario: String) = "home/${idUsuario}"
    fun categoria(idUsuario: String) = "categoria/${idUsuario}"
    fun profile(idUsuario: String) = "profile/${idUsuario}"
}
