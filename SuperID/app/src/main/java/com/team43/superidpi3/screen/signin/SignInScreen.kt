package com.team43.superidpi3.screen.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.components.Header
import com.team43.superidpi3.components.InputField
import com.team43.superidpi3.navigation.Routes
import com.team43.superidpi3.ui.theme.SuperIDBackground
import com.team43.superidpi3.ui.theme.SuperIDButtonBlue
import com.team43.superidpi3.ui.theme.SuperIDTextWhite

@Composable
fun SignInScreen(navController: NavController) {
    val ctx = LocalContext.current
    val SignInActions = remember { SignInActions(ctx, navController) }

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SuperIDBackground)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Header({ navController.navigate(Routes.Welcome) })
        Text(
            text = "Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = SuperIDTextWhite,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        // Email
        InputField(
            label = "Email",
            value = email,
            onValueChange = { email = it },
            placeholder = "example@superid.com",
            leadingIcon = Icons.Filled.Email
        )
        Spacer(modifier = Modifier.height(18.dp))
        // Senha
        InputField(
            label = "Senha",
            value = senha,
            onValueChange = { senha = it },
            placeholder = "Digite sua senha",
            leadingIcon = Icons.Filled.Lock,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            ClickableText(
                text = AnnotatedString("Esqueceu sua Senha?"),
                style = TextStyle(color = SuperIDButtonBlue, fontSize = 14.sp, textDecoration = TextDecoration.Underline),
                onClick = {
                    navController.navigate(Routes.ForgotPassword)
                }
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        BtnPrimary("Entrar", 54.dp, email.isNotBlank() && senha.isNotBlank(), { SignInActions.login(email, senha) })
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Ainda não tem conta? ", color = SuperIDTextWhite, fontSize = 14.sp)
            ClickableText(
                text = AnnotatedString("Registre-se agora"),
                style = TextStyle(color = SuperIDButtonBlue, fontSize = 14.sp, textDecoration = TextDecoration.Underline),
                onClick = { navController.navigate(Routes.SignUp)}
            )
        }
    }
}