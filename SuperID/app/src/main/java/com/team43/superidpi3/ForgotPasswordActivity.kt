package com.team43.superidpi3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.team43.superidpi3.ui.theme.SuperIDBackground
import com.team43.superidpi3.ui.theme.SuperIDButtonBlue
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.firestore.ktx.firestore
import androidx.compose.ui.graphics.Color

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForgotPasswordScreen()
        }
    }
}

fun verificarEmailNoFirestore(
    email: String,
    onResult: (existe: Boolean, verificado: Boolean) -> Unit
) {
    val db = Firebase.firestore
    db.collection("usuarios")
        .whereEqualTo("email", email)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val doc = documents.first()
                val verificado = doc.getBoolean("emailVerificado") ?: false
                onResult(true, verificado)
            } else {
                onResult(false, false)
            }
        }
        .addOnFailureListener {
            onResult(false, false)
        }
}

fun recuperarSenha(ctx: Context, email: String) {
    val auth = Firebase.auth

    verificarEmailNoFirestore(email) { existe, verificado ->
        when {
            !existe -> {
                Toast.makeText(ctx, "Email não encontrado", Toast.LENGTH_LONG).show()
            }
            !verificado -> {
                Toast.makeText(ctx, "Por favor, verifique seu email antes de solicitar a recuperação de senha", Toast.LENGTH_LONG).show()
            }
            else -> {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        Toast.makeText(
                            ctx,
                            "E-mail de recuperação enviado com sucesso.",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(ctx, SignInActivity::class.java)
                        ctx.startActivity(intent)
                    }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreen() {
    val ctx = LocalContext.current
    var email by remember { mutableStateOf("") }

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
                    val intent = Intent(ctx, SignInActivity::class.java)
                    ctx.startActivity(intent)
                },
                modifier = Modifier
                    .size(56.dp)
                    .padding(bottom = 20.dp)
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
            text = "Recuperar Senha",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = SuperIDTextWhite,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = "Digite seu email cadastrado para receber as instruções de recuperação de senha",
            color = SuperIDTextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 18.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {

            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "Aviso",
                tint = Color(0xFFFFEB3B),
                modifier = Modifier.size(25.dp),

            )
            Spacer(modifier = Modifier.width(8.dp))
            Spacer(modifier = Modifier.padding(bottom = 35.dp))
            Text(
                text = "Essa função só será realizada se o usuário realizou a verificação de E-mail",
                color = SuperIDTextWhite,
                fontSize = 11.sp

            )

        }

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
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                recuperarSenha(ctx, email)
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
            enabled = email.isNotBlank()
        ) {
            Text(text = "Enviar Email", color = SuperIDTextWhite, fontSize = 18.sp)
        }
    }
}



