package com.team43.superidpi3.screen.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.ui.theme.SuperIDTextWhite

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("AutoboxingStateCreation", "RememberReturnType")
@Composable
fun ProfileScreen(idUsuario: String, navController: NavController, padding: PaddingValues) {
    val ctx = LocalContext.current
    val usuarioState = remember { mutableStateOf<Map<String, Any>?>(null) }
    val ProfileActions = ProfileActions(ctx, navController)

    if (!idUsuario.isNullOrEmpty()) {
        ProfileActions.buscaUsuario(idUsuario) { dados ->
            usuarioState.value = dados
        }
    } else {
        Log.e("uid", "UID não encontrado")
    }

    Column(
        modifier = Modifier
            .padding(padding),
        horizontalAlignment = Alignment.Start
    ) {
        usuarioState.value?.let { usuario ->
            Text("Nome: ${usuario["nome"] ?: "Não informado"}", color = SuperIDTextWhite)
            Text("Email: ${usuario["email"] ?: "Não informado"}", color = SuperIDTextWhite)
            Text(text = "UID: $idUsuario", color = SuperIDTextWhite)

//            if (usuario["emailVerificado"] as Boolean) {
//                Button(
//                    onClick = {
//                        VerificarEmail(ctx).verifica { emailVerificado ->
//                            if (emailVerificado) {
//                                Toast.makeText(ctx, "Email verificado com sucesso!", Toast.LENGTH_SHORT).show()
//                            } else {
//                                Toast.makeText(ctx, "Seu email ainda não foi verificado.", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E94)),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 24.dp)
//                        .height(50.dp)
//                ) {
//                    Text(text = "Já verifiquei o email")
//                }
//            }
        }

        BtnPrimary("Sair", 54.dp, true, {ProfileActions.logout()})


    }

}
