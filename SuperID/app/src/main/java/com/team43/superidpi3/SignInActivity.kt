package com.team43.superidpi3


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
                Log.e(TAG, "Email ou senha inválidas, tente novamente", task.exception)
            }
        }
}

@Preview
@Composable
fun Auth() {
    val ctx = LocalContext.current

    Column(modifier = Modifier.padding(24.dp)) {
        var email by remember { mutableStateOf("") }
        var senha by remember { mutableStateOf("") }


        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") }
        )

        TextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") }
        )

        Button(onClick = {
            login(ctx, email, senha)
        }) {
            Text("Entrar")
        }

    }
}