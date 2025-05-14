package com.team43.superidpi3.screen.home

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.screen.home.components.SenhaCard
import com.team43.superidpi3.screen.profile.ProfileActions
import com.team43.superidpi3.utils.VerificarEmail

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("AutoboxingStateCreation", "RememberReturnType")
@Composable
fun HomeScreen(idUsuario: String, navController: NavController, padding: PaddingValues) {
    val ctx = LocalContext.current
    val categorias = remember {
        List(100) { i -> "Categoria $i" }
    }
    var categoriaSelecionada by remember { mutableStateOf(categorias[0]) }

    val usuarioState = remember { mutableStateOf<Map<String, Any>?>(null) }
    val ProfileActions = ProfileActions(ctx, navController)

    if (!idUsuario.isNullOrEmpty()) {
        ProfileActions.buscaUsuario(idUsuario) { dados ->
            usuarioState.value = dados
        }
    } else {
        Log.e("UID", "UID não encontrado")
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {

        // Categorias
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categorias) { categoria ->
                val isSelected = categoria == categoriaSelecionada
                Text(
                    text = categoria,
                    color = if (isSelected) Color.White else Color.Gray,
                    modifier = Modifier
                        .clickable { categoriaSelecionada = categoria }
                        .background(
                            if (isSelected) Color(0xFF0066FF) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de cartões
        val senhas = listOf("auth 01", "auth 02", "auth 03")
        senhas.forEach { login ->
            SenhaCard(title = login, description = "Descrição")
            Spacer(modifier = Modifier.height(2.dp))
        }

        Box(
            modifier = Modifier.padding(16.dp)
        ){
            usuarioState.value?.let { usuario ->
                BtnPrimary(if(usuario["emailVerificado"] as Boolean) "Email validado!" else "Verificar se email foi validado", 54.dp, true, {
                    VerificarEmail(ctx).verifica { emailVerificado ->
                        if (emailVerificado) {
                            Toast.makeText(ctx, "Email verificado com sucesso!", Toast.LENGTH_SHORT).show()

                            if(usuario["emailVerificado"] as? Boolean == false) {
                                ProfileActions.buscaUsuario(idUsuario) { dados ->
                                    usuarioState.value = dados
                                }
                            }
                        } else {
                            Toast.makeText(ctx, "Seu email ainda não foi verificado.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

}

