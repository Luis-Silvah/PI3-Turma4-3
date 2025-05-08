package com.team43.superidpi3.screen.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.team43.superidpi3.utils.VerificarEmail


@Composable
fun HomeScreen(idUsuario: String, navController: NavController) {
    val ctx = LocalContext.current
    val HomeActions = remember { HomeActions(ctx, navController) }

    val usuarioState = remember { mutableStateOf<Map<String, Any>?>(null) }

    if (!idUsuario.isNullOrEmpty()) {
        HomeActions.buscaUsuario(idUsuario) { dados ->
            usuarioState.value = dados
        }
    } else {
        Log.e("uid", "UID não encontrado")
    }

    Column {
        usuarioState.value?.let { usuario ->
            Text("Nome: ${usuario["nome"] ?: "Não informado"}")
            Text("Email: ${usuario["email"] ?: "Não informado"}")
        }

        Text(text = "UID: $idUsuario")

        Button(
            onClick = {
                VerificarEmail(ctx).verifica { emailVerificado ->
                    if (emailVerificado) {
                        Toast.makeText(ctx, "Email verificado com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(ctx, "Seu email ainda não foi verificado.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
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
