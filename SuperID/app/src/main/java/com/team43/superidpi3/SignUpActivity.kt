package com.team43.superidpi3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CadastroUsuario()
        }
    }
}

fun register(ctx: Context, nome: String, email: String, senha: String) {
    val auth = Firebase.auth
    val TAG = "FIREBASE-AUTH"

    auth.createUserWithEmailAndPassword(email, senha)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val usuario = auth.currentUser
                Log.d(TAG, "Usuário criado com sucesso! ${usuario!!.uid}")

                val intent = Intent(ctx, WelcomeActivity::class.java)
                intent.putExtra("uid", usuario.uid)
                ctx.startActivity(intent)
                if (ctx is SignUpActivity) {
                    ctx.finish()
                }

                saveUsuario(nome, email, usuario.uid)
            } else {
                Log.e(TAG, "Não foi possível criar usuário", task.exception)
                Toast.makeText(ctx, "Erro ao criar conta: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
}

fun saveUsuario(nome: String, email: String, uid: String) {
    val db = Firebase.firestore
    val TAG = "FIREBASE-FIRESTORE"

    val usuario = hashMapOf(
        "nome" to nome,
        "email" to email,
        "uid" to uid
    )

    db.collection("usuarios")
        .document(uid)
        .set(usuario)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Usuário salvo com sucesso!")
            } else {
                Log.e(TAG, "Não foi possível salvar usuário", task.exception)
            }
        }
}

@Preview(showBackground = true)
@Composable
fun CadastroUsuario() {
    val ctx = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var termosAceitos by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp) // altura da barra (ajuste conforme o Figma)
                .background(Color(0xFF2C3E94)) // código azul do seu projeto
        )

        Spacer(modifier = Modifier.height(16.dp)) // espaço depois da barra



        Image(
            painter = painterResource(id = R.drawable.superid_logo), // use a imagem do Figma
            contentDescription = "Logo SuperID",
            modifier = Modifier
                .height(220.dp)
                .width(500.dp)
                .align(Alignment.CenterHorizontally)
        )


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Criar uma conta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha Mestre") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = termosAceitos,
                onCheckedChange = { termosAceitos = it }
            )
            Text(text = "Li e aceito os ")
            Text(
                text = "termos de uso",
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && termosAceitos) {
                    register(ctx, nome, email, senha)
                } else {
                    Toast.makeText(ctx, "Preencha todos os campos e aceite os termos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = termosAceitos
        ) {
            Text(text = "Cadastrar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(text = "Já tem uma conta? ")
            ClickableText(
                text = AnnotatedString("Entrar"),
                onClick = {
                    val intent = Intent(ctx, SignInActivity::class.java)
                    ctx.startActivity(intent)
                },
                style = TextStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)
            )
        }
    }
}
