package com.team43.superidpi3.screen.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team43.superidpi3.domain.Senha
import com.team43.superidpi3.screen.categoria.CategoriaActions
import com.team43.superidpi3.screen.home.components.SenhaCard
import com.team43.superidpi3.screen.profile.ProfileActions
import com.team43.superidpi3.screen.senha.SenhaActions

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("AutoboxingStateCreation", "RememberReturnType")
@Composable
fun HomeScreen(idUsuario: String, navController: NavController, padding: PaddingValues) {
    val ctx = LocalContext.current
    val CategoriaFixa = "Todas"

    val categoriaActions = remember { CategoriaActions() }
    val categoriasFirebaseState = remember { mutableStateOf<List<String>>(emptyList()) }

    var categoriaSelecionada by remember { mutableStateOf(CategoriaFixa) }

    LaunchedEffect(idUsuario) {
        categoriaActions.buscarCategoriasDoUsuario(idUsuario) { lista ->
            categoriasFirebaseState.value = lista
            // Se a categoria selecionada não estiver na lista, resetar para "Todas"
            if (lista.isNotEmpty() && !lista.contains(categoriaSelecionada)) {
                categoriaSelecionada = CategoriaFixa
            }
        }
    }

    val senhasState = remember { mutableStateOf<List<Senha>>(emptyList()) }
    val senhaActions = remember { SenhaActions() }

    LaunchedEffect(idUsuario) {
        senhaActions.buscarSenhasDoUsuario(idUsuario) { lista ->
            senhasState.value = lista
            Log.d("HomeScreen", "Senhas carregadas: ${lista.size}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            item {
                val isSelected = categoriaSelecionada == CategoriaFixa
                Text(
                    text = CategoriaFixa,
                    color = if (isSelected) Color.White else Color.Gray,
                    modifier = Modifier
                        .clickable { categoriaSelecionada = CategoriaFixa }
                        .background(
                            if (isSelected) Color(0xFF0066FF) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }

            items(categoriasFirebaseState.value) { categoria ->
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

        val senhasFiltradas = if (categoriaSelecionada == CategoriaFixa) {
            senhasState.value
        } else {
            senhasState.value.filter { it.categoria.equals(categoriaSelecionada, ignoreCase = true) }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (senhasFiltradas.isEmpty()) {
                item {
                    Text(
                        text = "Nenhuma senha nessa categoria",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = MaterialTheme.typography.titleLarge.fontSize * 0.7f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                items(senhasFiltradas) { senha ->
                    SenhaCard(
                        title = senha.nome,
                        description = senha.descricao.ifBlank { "Sem descrição" },
                        password = senha.senha,
                        senhaId = senha.id,
                        categoriaNome = senha.categoria,
                        navController = navController
                    )
                }
            }
        }
    }

}


