package com.team43.superidpi3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team43.superidpi3.screen.home.HomeScreen
import com.team43.superidpi3.screen.qrcode.CameraAppScreen
import com.team43.superidpi3.screen.signin.SignInScreen
import com.team43.superidpi3.screen.signin.navigation.ForgotPasswordScreen
import com.team43.superidpi3.screen.signup.SignUpScreen
import com.team43.superidpi3.screen.splash.SplashScreen
import com.team43.superidpi3.screen.welcome.WelcomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            SplashScreen(navController)
        }

        composable(Routes.Welcome) {
            WelcomeScreen(navController)
        }

        composable(
            route = Routes.Home,
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) {
            val idUsuario = it.arguments?.getString("idUsuario") ?: ""

            HomeScreen(idUsuario, navController)
        }
        composable(Routes.SignUp) {
            SignUpScreen(navController)
        }

        composable(Routes.SignIn) {
            SignInScreen(navController)
        }
        composable(Routes.qrcode) {
            CameraAppScreen(navController)
        }


        composable(Routes.ForgotPassword) {
            ForgotPasswordScreen(navController)
        }
    }
}

