package com.team43.superidpi3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Welcome()
        }
    }
}

fun buscaUsuario(uid: String, onResult: (Map<String, Any>?) -> Unit) {
    val db = Firebase.firestore
    val TAG = "FIRESTORE-BUSCA"
    val docRef = db.collection("usuarios").document(uid)

    docRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val document = task.result
            Log.d(TAG, "Usuário encontrado: ${document?.data}")
            onResult(document?.data)
        } else {
            Log.e(TAG, "Não foi possivel encontrar usuário", task.exception)
        }
    }
}

@Composable
fun Welcome() {
    val context = LocalContext.current
    val uid = remember {
        (context as? Activity)?.intent?.getStringExtra("uid")
    }
    val usuarioState = remember { mutableStateOf<Map<String, Any>?>(null) }

    if (!uid.isNullOrEmpty()) {
        buscaUsuario(uid) { dados ->
            usuarioState.value = dados
        }
    } else {
        Log.e("uid", "UID não encontrado")
    }

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Bem-vindo ao programa experimental Firebase")

        usuarioState.value?.let { usuario ->
            Text("Nome: ${usuario["nome"] ?: "Não informado"}")
            Text("Email: ${usuario["email"] ?: "Não informado"}")
        }

        Button(
            onClick = { verificarEmail(context) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E94)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(50.dp)
        ) {
            Text(text = "Já verifiquei o email")
        }
    }


}

//Função que verifica se o usuario confirmou email com botao
//So deixei pronto pra mostrar que ja funciona
// TODO: Deixar isso de uma maneira melhor com funcao de reenviar email de verificao

private fun verificarEmail(context: Context) {
    verificarEmailVerificado(context) { emailVerificado ->
        if (emailVerificado) {
            Toast.makeText(context, "Email verificado com sucesso!", Toast.LENGTH_SHORT).show()
            // NÃO muda de tela, só exibe a mensagem
        } else {
            Toast.makeText(context, "Seu email ainda não foi verificado.", Toast.LENGTH_SHORT).show()
        }
    }
}

