package com.team43.superidpi3.screen.categoria

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.team43.superidpi3.components.Header
import com.team43.superidpi3.components.InputField
import com.team43.superidpi3.navigation.Routes
import com.team43.superidpi3.ui.theme.SuperIDTextWhite

fun deletarCategoria(
    firestore: FirebaseFirestore,
    userId: String,
    categoriaNome: String,
    onSuccess: () -> Unit,
    onError: (Exception) -> Unit
) {
    firestore.collection("usuarios")
        .document(userId)
        .collection("categorias")
        .whereEqualTo("nome", categoriaNome)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val categoriaDoc = querySnapshot.documents[0]
                val categoriaId = categoriaDoc.id

                firestore.collection("usuarios")
                    .document(userId)
                    .collection("categorias")
                    .document(categoriaId)
                    .delete()
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onError(e)
                    }
            } else {
                onError(Exception("Categoria com nome $categoriaNome não encontrada"))
            }
        }
        .addOnFailureListener { e ->
            onError(e)
        }
}

@Composable
fun DeleteCategoriaScreen(
    navController: NavController,
    padding: PaddingValues,
    categoriaNome: String,
    idUsuario: String
) {
    var confirmText by remember { mutableStateOf("") }
    val firestore = FirebaseFirestore.getInstance()
    val userId = idUsuario

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {

        Header {
            navController.popBackStack() // ou navegar para tela anterior
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Deletar Categoria",
            color = SuperIDTextWhite,
            fontSize = 35.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Digite \"confirmar\" no campo abaixo para concluir o processo de exclusão:",
            color = SuperIDTextWhite.copy(alpha = 0.8f),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Aviso",
                tint = Color(0xFFFFA000), // amarelo escuro/material warning color
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "AVISO: Esse processo irá excluir todas as senhas salvas nessa categoria",
                color = Color(0xFFFFC107), // amarelo claro/material warning light
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            label = "",
            value = confirmText,
            onValueChange = { confirmText = it },
            placeholder = "Confirmar",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                deletarCategoria(
                    firestore = firestore,
                    userId = userId,
                    categoriaNome = categoriaNome,
                    onSuccess = {
                        navController.popBackStack()
                    },
                    onError = { e ->
                        println("Erro ao deletar categoria: ${e.message}")
                    }
                )
            },
            enabled = confirmText.lowercase() == "confirmar",
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White
            )
        ) {
            Text("Excluir")
        }

    }
}
