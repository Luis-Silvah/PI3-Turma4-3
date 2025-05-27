package com.team43.superidpi3.screen.categoria

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.components.Header
import com.team43.superidpi3.components.InputField
import com.team43.superidpi3.navigation.Routes
import com.team43.superidpi3.ui.theme.SuperIDTextWhite

fun salvarCategoria(
    idUsuario: String,
    nomeCategoria: String,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val categoriaRef = db.collection("usuarios")
        .document(idUsuario)
        .collection("categorias")
        .document()

    val novaCategoria = hashMapOf(
        "id" to categoriaRef.id,
        "nome" to nomeCategoria
    )

    categoriaRef.set(novaCategoria)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { exception -> onFailure(exception) }
}

@Composable
fun AddCategoriaScreen(
    navController: NavController,
    idUsuario: String,
    padding: PaddingValues = PaddingValues()
) {
    var nomeCategoria by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Header {
            navController.navigate(Routes.categoria(idUsuario))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Criar Categoria",
            fontSize = 35.sp,
            color = SuperIDTextWhite
        )

        Spacer(modifier = Modifier.height(40.dp))

        InputField(
            label = "Nome",
            value = nomeCategoria,
            onValueChange = { nomeCategoria = it },
            placeholder = "Nova Categoria",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        BtnPrimary(
            label = "Adicionar",
            height = 54.dp,
            enabled = nomeCategoria.isNotBlank(),
            onClick = {
                salvarCategoria(
                    idUsuario = idUsuario,
                    nomeCategoria = nomeCategoria.trim(),
                    onSuccess = {
                        navController.popBackStack()
                    },
                    onFailure = {
                        Log.e("AddCategoriaScreen", "Erro ao salvar categoria", it)
                    }
                )
            }
        )
    }
}
