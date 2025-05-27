package com.team43.superidpi3.screen.home

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.components.Layout
import com.team43.superidpi3.domain.Senha
import com.team43.superidpi3.navigation.Routes
import com.team43.superidpi3.screen.home.components.SenhaCard
import com.team43.superidpi3.screen.profile.ProfileActions
import com.team43.superidpi3.screen.senha.SenhaActions
import com.team43.superidpi3.ui.theme.SuperIDGrayPrimary
import com.team43.superidpi3.ui.theme.SuperIDWhite
import com.team43.superidpi3.utils.VerificarEmail

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("AutoboxingStateCreation", "RememberReturnType")
@Composable
fun HomeScreen(idUsuario: String, navController: NavController, padding: PaddingValues) {
    val ctx = LocalContext.current


    val categorias = listOf("Redes sociais", "Bancos", "Jogos", "E-mails", "Outros")
    var categoriaSelecionada by remember { mutableStateOf(categorias[0]) }

    val usuarioState = remember { mutableStateOf<Map<String, Any>?>(null) }
    val senhasState = remember { mutableStateOf<List<Senha>>(emptyList()) }

    val senhaActions = remember { SenhaActions() }
    val profileActions = remember { ProfileActions(ctx, navController) }

    // Busca usuário


    LaunchedEffect(idUsuario) {
        senhaActions.buscarSenhasDoUsuario(idUsuario) { lista ->
            senhasState.value = lista
            Log.d("HomeScreen", "Senhas carregadas: ${lista.size}")
        }
    }


    Layout(
        routeIndex = 0,
        navController = navController,
        title = "",
        idUsuario = idUsuario,
        onFabClick = {
            navController.navigate(Routes.addSenha(idUsuario))
        }
    ) { paddingInner ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInner)
        ) {
            // CATEGORIAS
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

            // LISTA DE SENHAS FILTRADAS
            val senhasFiltradas = senhasState.value.filter {
                it.categoria.equals(categoriaSelecionada, ignoreCase = true)
            }

            if (senhasFiltradas.isEmpty()) {
                Text(
                    text = "Nenhuma senha nessa categoria",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = MaterialTheme.typography.titleLarge.fontSize * 0.7f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            } else {
                senhasFiltradas.forEach { senha ->
                    SenhaCard(title = senha.nome, description = "descrição", password = senha.senha, senhaId = senha.id,
                        navController = navController)
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }

            // BOTÃO DE VERIFICAÇÃO
            Box(modifier = Modifier.padding(16.dp)) {
                usuarioState.value?.let { usuario ->
                    BtnPrimary(
                        if (usuario["emailVerificado"] as Boolean) "Email validado!" else "Verificar se email foi validado",
                        54.dp,
                        true
                    ) {
                        VerificarEmail(ctx).verifica { emailVerificado ->
                            if (emailVerificado) {
                                Toast.makeText(ctx, "Email verificado com sucesso!", Toast.LENGTH_SHORT).show()
                                if (usuario["emailVerificado"] as? Boolean == false) {
                                    profileActions.buscaUsuario(idUsuario) { dados ->
                                        usuarioState.value = dados
                                    }
                                }
                            } else {
                                Toast.makeText(ctx, "Seu email ainda não foi verificado.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}
