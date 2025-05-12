package com.team43.superidpi3.screen.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.components.Header
import com.team43.superidpi3.components.InputField
import com.team43.superidpi3.components.TermosDialog
import com.team43.superidpi3.navigation.Routes
import com.team43.superidpi3.ui.theme.SuperIDBackground
import com.team43.superidpi3.ui.theme.SuperIDButtonBlue
import com.team43.superidpi3.ui.theme.SuperIDTextWhite


@Composable
fun SignUpScreen(navController: NavController) {
    val ctx = LocalContext.current
    val SignUpActions = remember { SignUpActions(ctx, navController) }

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var termosAceitos by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SuperIDBackground)
            .padding(horizontal = 24.dp, vertical = 0.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Header({ navController.navigate(Routes.SignIn) })
        Text(
            text = "Cadastrar Conta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = SuperIDTextWhite,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        // Nome
        InputField(
            label = "Nome",
            value = nome,
            onValueChange = { nome = it },
            placeholder = "Digite seu nome"

        )
        Spacer(modifier = Modifier.height(18.dp))
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
        Spacer(modifier = Modifier.height(18.dp))
        // Checkbox e termos com links
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Checkbox(
                checked = termosAceitos,
                onCheckedChange = { termosAceitos = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = SuperIDButtonBlue,
                    uncheckedColor = SuperIDTextWhite,
                    checkmarkColor = SuperIDTextWhite
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            val annotatedString = buildAnnotatedString {
                withStyle(SpanStyle(color = SuperIDTextWhite)) {
                    append("Você aceita os ")
                }
                pushStringAnnotation(tag = "termos", annotation = "termos")
                withStyle(
                    SpanStyle(
                        color = SuperIDButtonBlue,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append("Termos de uso e reconhece a declaração de privacidade e política de cookies")
                }
                pop()
            }
            ClickableText(
                text = annotatedString,
                style = TextStyle(fontSize = 12.sp),
                onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "termos", start = offset, end = offset)
                        .firstOrNull()?.let {
                            showTermsDialog = true
                        }
                }
            )
        }
        if (showTermsDialog) {
            TermosDialog(
                show = showTermsDialog,
                onDismiss = { showTermsDialog = false }
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        // Botão
        BtnPrimary("Criar conta", 54.dp,termosAceitos, {
            if (nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && termosAceitos) {
                SignUpActions.registrar(nome, email, senha)
            } else {
                Toast.makeText(ctx, "Preencha todos os campos e aceite os termos", Toast.LENGTH_SHORT).show()
            }
        })
        Spacer(modifier = Modifier.height(24.dp))
        // Link para login
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Já tem uma conta? ", color = SuperIDTextWhite, fontSize = 14.sp)
            ClickableText(
                text = AnnotatedString("Entrar"),
                onClick = { navController.navigate(Routes.SignIn) },
                style = TextStyle(
                    color = SuperIDButtonBlue,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 14.sp
                )
            )
        }
    }
}