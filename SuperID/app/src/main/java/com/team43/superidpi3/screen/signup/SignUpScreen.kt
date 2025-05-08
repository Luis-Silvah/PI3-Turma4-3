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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team43.superidpi3.components.Header
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
    var senhaVisivel by remember { mutableStateOf(false) }

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
        Text(
            text = "Nome",
            color = SuperIDTextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = nome,
            onValueChange = { nome = it },
            placeholder = { Text("Digite seu nome", color = SuperIDTextWhite.copy(alpha = 0.7f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
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
        // Email
        Text(
            text = "Email",
            color = SuperIDTextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("example@superid.com", color = SuperIDTextWhite.copy(alpha = 0.7f)) },
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
                withStyle(SpanStyle(color = SuperIDButtonBlue, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Medium)) {
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
        Button(
            onClick = {
                if (nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && termosAceitos) {
                    SignUpActions.registrar(nome, email, senha)
                } else {
                    Toast.makeText(ctx, "Preencha todos os campos e aceite os termos", Toast.LENGTH_SHORT).show()
                }
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
            enabled = termosAceitos
        ) {
            Text(text = "Criar conta", color = SuperIDTextWhite, fontSize = 18.sp)
        }
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
                style = TextStyle(color = SuperIDButtonBlue, textDecoration = TextDecoration.Underline, fontSize = 14.sp)
            )
        }
    }
}