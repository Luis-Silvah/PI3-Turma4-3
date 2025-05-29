package com.team43.superidpi3.screen.profile

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import com.team43.superidpi3.utils.VerificarEmail

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("AutoboxingStateCreation", "RememberReturnType")
@Composable
fun ProfileScreen(idUsuario: String, navController: NavController, padding: PaddingValues) {
    val ctx = LocalContext.current

    val auth = Firebase.auth
    val usuarioAuth = auth.currentUser
    val usuarioState = remember { mutableStateOf<Map<String, Any>?>(null) }
    val ProfileActions = ProfileActions(ctx, navController)

    if (!idUsuario.isNullOrEmpty()) {
        ProfileActions.buscaUsuario(idUsuario) { dados ->
            usuarioState.value = dados
        }
    } else {
        Log.e("uid", "UID não encontrado")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícone de perfil
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Perfil",
                modifier = Modifier
                    .size(80.dp),
                tint = Color(0xFF6C63FF)
            )

            Spacer(modifier = Modifier.height(24.dp))

            usuarioState.value?.let { usuario ->
                PerfilCampo("Seu Nome", usuario["nome"]?.toString() ?: "Não informado")
                PerfilCampo("Email", usuario["email"]?.toString() ?: "Não informado")

                if ((usuario["emailVerificado"] as? Boolean) == true) {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text(
                            text = "Verificação de Email",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFDFF5E3))
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Email verificado",
                                tint = Color(0xFF2E7D32), // Verde
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verificado", color = Color(0xFF2E7D32), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text(
                            text = "Verificação de Email",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFEAEA))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Email não verificado",
                                    tint = Color.Red,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Não Verificado", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
                            }

                            Row {
                                IconButton(onClick = { VerificarEmail(ctx).verifica { emailVerificado ->
                                    if (!emailVerificado) {
                                        Toast.makeText(ctx, "Seu email ainda não foi verificado.", Toast.LENGTH_SHORT).show()
                                    } else {
                                        ProfileActions.buscaUsuario(idUsuario) { dados ->
                                            usuarioState.value = dados
                                        }
                                    }
                                } }) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Verificar novamente",
                                        tint = Color.Gray
                                    )
                                }

                                Button(
                                    onClick = {
                                        usuarioAuth?.sendEmailVerification()
                                            ?.addOnCompleteListener { verificationTask ->
                                                if (verificationTask.isSuccessful) {
                                                    Toast.makeText(ctx, "Email de verificação enviado", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    Toast.makeText(ctx, "Limite de reenvios atingido. Tente novamente mais tarde.", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("Reenviar", color = Color.White)
                                }
                            }
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(32.dp))

            BtnPrimary("Sair", 54.dp, true) { ProfileActions.logout() }
        }
    }

}



@Composable
fun PerfilCampo(titulo: String, valor: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF1F3F6))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}

