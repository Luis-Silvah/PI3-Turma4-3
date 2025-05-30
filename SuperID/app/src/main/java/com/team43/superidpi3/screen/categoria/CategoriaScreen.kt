package com.team43.superidpi3.screen.categoria


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team43.superidpi3.ui.theme.SuperIDTextWhite

data class CategoriaItem(
    val nome: String,
    val isPadrao: Boolean
)

@Composable
fun CategoriaScreen(navController: NavController, idUsuario: String, padding: PaddingValues) {
    val categoriaActions = remember { CategoriaActions() }
    val categoriasFirebaseState = remember { mutableStateOf(listOf<CategoriaItem>()) }

    LaunchedEffect(idUsuario) {
        categoriaActions.buscarCategoriasComPadrao(idUsuario) { lista ->
            categoriasFirebaseState.value = lista.sortedByDescending { it.isPadrao }
        }
    }

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
                        text = categoria.nome,
                        color = SuperIDTextWhite,
                        fontSize = 18.sp
                    )
                    if (categoria.isPadrao) {
                        IconButton(onClick = { }, enabled = false) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Categoria padrão - não pode excluir",
                                tint = Gray
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            navController.navigate("deletecategoria/${categoria.nome}/$idUsuario")
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
}



