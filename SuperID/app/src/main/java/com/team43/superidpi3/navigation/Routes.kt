package com.team43.superidpi3.navigation

object Routes {
    const val Splash = "splash"
    const val Welcome = "welcome"

    const val Home = "home/{idUsuario}"
    const val SignIn = "signin"
    const val ForgotPassword = "forgot-password"
    const val SignUp = "signup"

    fun home(idUsuario: String) = "home/${idUsuario}"
}
