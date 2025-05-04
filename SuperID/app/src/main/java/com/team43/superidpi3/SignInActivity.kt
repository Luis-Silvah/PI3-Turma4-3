package com.team43.superidpi3


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.team43.superidpi3.ui.theme.SuperIDBackground
import com.team43.superidpi3.ui.theme.SuperIDButtonBlue
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.tooling.preview.Preview

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Auth()
        }
    }
}

fun login(ctx: Context, email: String, senha: String) {
    var auth = Firebase.auth
    val TAG = "FIREBASE-AUTH"

    auth.signInWithEmailAndPassword(email, senha)
        .addOnCompleteListener { task ->
            if(task.isSuccessful){
                val usuario = auth.currentUser
                Log.d(TAG, "Permissão para login, ${usuario!!.uid}")
                val intent = Intent(ctx, WelcomeActivity::class.java)
                intent.putExtra("uid", usuario!!.uid)
                ctx.startActivity(intent)
            } else {
                // Log do erro completo para debug
                Log.e(TAG, "Erro de login: ${task.exception?.javaClass?.simpleName}", task.exception)
                Log.e(TAG, "Mensagem de erro: ${task.exception?.message}")
                
                val errorCode = (task.exception as? com.google.firebase.auth.FirebaseAuthException)?.errorCode
                Log.e(TAG, "Código de erro: $errorCode")

                when (errorCode) {
                    "ERROR_USER_DOES_NOT_EXIST" -> {
                        Toast.makeText(ctx, "Email não encontrado no sistema", Toast.LENGTH_LONG).show()
                    }
                    "ERROR_INVALID_PASSWORD" -> {
                        Toast.makeText(ctx, "Senha incorreta", Toast.LENGTH_LONG).show()
                    }
                    "ERROR_INVALID_EMAIL" -> {
                        Toast.makeText(ctx, "Formato de email inválido", Toast.LENGTH_LONG).show()
                    }
                    "ERROR_NETWORK_REQUEST_FAILED" -> {
                        Toast.makeText(ctx, "Erro de conexão. Verifique sua internet", Toast.LENGTH_LONG).show()
                    }
                    "ERROR_USER_DISABLED" -> {
                        Toast.makeText(ctx, "Conta desativada. Entre em contato com o suporte", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        // Se não for nenhum dos erros conhecidos, mostra a mensagem genérica
                        Log.e(TAG, "Erro não tratado: $errorCode")
                        Toast.makeText(ctx, "Erro ao fazer login. Tente novamente mais tarde", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
}

@Preview(showBackground = true)
@Composable
fun Auth() {
    val ctx = LocalContext.current
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = {
                    val intent = Intent(ctx, MainActivity::class.java)
                    ctx.startActivity(intent)
                },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = SuperIDTextWhite.copy(alpha = 0.05f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = SuperIDTextWhite,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.superid_logo),
                contentDescription = "Logo SuperID",
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
            )
        }
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
        androidx.compose.material3.TextField(
            value = email,
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
        androidx.compose.material3.TextField(
            value = senha,
            onValueChange = { senha = it },
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
                    val intent = Intent(ctx, ForgotPasswordActivity::class.java)
                    ctx.startActivity(intent)
                }
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            onClick = {
                login(ctx, email, senha)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = SuperIDButtonBlue,
                disabledContainerColor = SuperIDButtonBlue.copy(alpha = 0.3f),
                contentColor = SuperIDTextWhite,
                disabledContentColor = SuperIDTextWhite.copy(alpha = 0.5f)
            ),
            shape = androidx.compose.material3.MaterialTheme.shapes.medium,
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
                onClick = {
                    val intent = Intent(ctx, SignUpActivity::class.java)
                    ctx.startActivity(intent)
                }
            )
        }
    }
}