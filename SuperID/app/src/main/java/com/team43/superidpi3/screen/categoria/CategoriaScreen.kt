package com.team43.superidpi3.screen.categoria

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team43.superidpi3.ui.theme.SuperIDTextWhite


@Composable
fun CategoriaScreen(navController: NavController, idUsuario: String, padding: PaddingValues) {
//    val categoriasFixas = listOf("Redes Sociais", "Bancos", "Trabalho")

    val categoriaActions = remember { CategoriaActions() }
    val categoriasFirebaseState = remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(idUsuario) {
        categoriaActions.buscarCategoriasDoUsuario(idUsuario) { lista ->
            categoriasFirebaseState.value = lista
        }
    }

//    val todasCategorias = categoriasFixas + categoriasFirebaseState.value

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Lista de Categorias:",
            color = SuperIDTextWhite,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categoriasFirebaseState.value) { categoria ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = categoria,
                        color = SuperIDTextWhite,
                        fontSize = 18.sp
                    )
                    IconButton(onClick = {
                        // função excluir categoria
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Excluir categoria",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}
