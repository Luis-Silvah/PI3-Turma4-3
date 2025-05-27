package com.team43.superidpi3.screen.senha


import androidx.compose.foundation.layout.*
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

@Composable
fun DeleteSenhaScreen(
    navController: NavController,
    padding: PaddingValues,
    senhaId: String,
) {
    var confirmText by remember { mutableStateOf("") }
    val firestore = FirebaseFirestore.getInstance()
    val idUsuario = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {

        Header {
            navController.navigate(Routes.home(idUsuario))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Deletar Senha",
            color = SuperIDTextWhite,
            fontSize = 35.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Para confirmar, digite \"confirmar\" abaixo:",
            color = SuperIDTextWhite.copy(alpha = 0.8f),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            label = "",
            value = confirmText,
            onValueChange = { confirmText = it },
            placeholder = "Digite 'confirmar'",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button
                firestore.collection("usuarios")
                    .document(userId)
                    .collection("senhas")
                    .document(senhaId)
                    .delete()
                    .addOnSuccessListener {
                        navController.popBackStack()
                    }
                    .addOnFailureListener {

                    }
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

