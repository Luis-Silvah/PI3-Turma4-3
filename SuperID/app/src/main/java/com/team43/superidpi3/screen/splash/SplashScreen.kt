package com.team43.superidpi3.screen.splash

import android.util.Log
import android.widget.Toast
import com.team43.superidpi3.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.team43.superidpi3.navigation.Routes
import com.team43.superidpi3.ui.theme.SuperIDBackground
import com.team43.superidpi3.ui.theme.SuperIDButtonBlue
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import com.team43.superidpi3.utils.VerificarEmail
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val ctx = LocalContext.current

    LaunchedEffect(true) {
        delay(2000)
        val usuario = Firebase.auth.currentUser
        if (usuario != null) {

            VerificarEmail(ctx).verifica { emailVerificado ->
                if (!emailVerificado) {
                    Toast.makeText(ctx, "Seu email ainda não foi verificado.", Toast.LENGTH_SHORT).show()
                }
             }

            // Usuário está logado
            navController.navigate(Routes.home(usuario.uid)) {
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
            .background(SuperIDBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(140.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = SuperIDTextWhite,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Super")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = SuperIDButtonBlue,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Id")
                    }
                },
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}
