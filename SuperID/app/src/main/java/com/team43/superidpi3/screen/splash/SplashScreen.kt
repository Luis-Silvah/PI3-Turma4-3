package com.team43.superidpi3.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.team43.superidpi3.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(true) {
        delay(2000) // Aguarda 2 segundos
//        navController.navigate(Routes.Welcome) {
//            popUpTo(Routes.Splash) { inclusive = true }
//        }

        val user = Firebase.auth.currentUser
        if (user != null) {
            // Usuário está logado
            navController.navigate(Routes.home(user.uid)) {
                popUpTo(Routes.Splash) { inclusive = true }
            }
        } else {
            // Usuário não logado
            navController.navigate(Routes.Welcome) {
                popUpTo(Routes.Splash) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text("SuperId", fontSize = 32.sp, fontWeight = FontWeight.Bold)
    }
}
