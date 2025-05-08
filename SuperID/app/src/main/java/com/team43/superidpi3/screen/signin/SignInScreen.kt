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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team43.superidpi3.components.Header
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
    var senhaVisivel by remember { mutableStateOf(false) }

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
        Text(
            text = "Email",
            color = SuperIDTextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = email,
            placeholder = { Text("example@superid.com", color = SuperIDTextWhite.copy(alpha = 0.7f)) },
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = null, tint = SuperIDTextWhite.copy(alpha = 0.7f))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SuperIDBackground,
                unfocusedContainerColor = SuperIDBackground,
                focusedIndicatorColor = SuperIDButtonBlue,
                unfocusedIndicatorColor = SuperIDTextWhite.copy(alpha = 0.2f),
                focusedTextColor = SuperIDTextWhite,
                unfocusedTextColor = SuperIDTextWhite,
                cursorColor = SuperIDTextWhite,
                focusedLabelColor = SuperIDTextWhite,
                unfocusedLabelColor = SuperIDTextWhite
            )
        )
        Spacer(modifier = Modifier.height(18.dp))
        // Senha
        Text(
            text = "Senha",
            color = SuperIDTextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = senha,
            onValueChange = { senha = it },
            placeholder = { Text("Digite sua senha", color = SuperIDTextWhite.copy(alpha = 0.7f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = null, tint = SuperIDTextWhite.copy(alpha = 0.7f))
            },
            trailingIcon = {
                val image = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (senhaVisivel) "Ocultar senha" else "Mostrar senha"
                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                    Icon(imageVector = image, contentDescription = description, tint = SuperIDTextWhite.copy(alpha = 0.7f))
                }
            },
            visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SuperIDBackground,
                unfocusedContainerColor = SuperIDBackground,
                focusedIndicatorColor = SuperIDButtonBlue,
                unfocusedIndicatorColor = SuperIDTextWhite.copy(alpha = 0.2f),
                focusedTextColor = SuperIDTextWhite,
                unfocusedTextColor = SuperIDTextWhite,
                cursorColor = SuperIDTextWhite,
                focusedLabelColor = SuperIDTextWhite,
                unfocusedLabelColor = SuperIDTextWhite
            )
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
        Button(
            onClick = {
                SignInActions.login(email, senha)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SuperIDButtonBlue,
                disabledContainerColor = SuperIDButtonBlue.copy(alpha = 0.3f),
                contentColor = SuperIDTextWhite,
                disabledContentColor = SuperIDTextWhite.copy(alpha = 0.5f)
            ),
            shape = MaterialTheme.shapes.medium,
            enabled = email.isNotBlank() && senha.isNotBlank()
        ) {
            Text(text = "Entrar", color = SuperIDTextWhite, fontSize = 18.sp)
        }
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