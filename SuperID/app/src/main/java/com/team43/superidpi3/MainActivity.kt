package com.team43.superidpi3


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AcessarConta()
        }
    }
}

@Composable
fun AcessarConta() {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            "Acessar conta"
        )

        Button(onClick = {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Cadastrar")
        }

        Button(onClick = {
            val intent = Intent(context, SignInActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Entrar")
        }
    }
}