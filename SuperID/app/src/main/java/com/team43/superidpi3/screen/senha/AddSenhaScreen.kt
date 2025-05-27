package com.team43.superidpi3.screen.senha

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
import com.team43.superidpi3.screen.categoria.CategoriaActions
import com.team43.superidpi3.ui.theme.SuperIDTextWhite

fun salvarSenha(
    idUsuario: String,
    nome: String,
    senha: String,
    categoria: String,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val senhaRef = db.collection("usuarios")
        .document(idUsuario)
        .collection("senhas")
        .document()

    val novaSenha = hashMapOf(
        "id" to senhaRef.id,
        "nome" to nome,
        "senha" to senha,
        "categoria" to categoria
    )

    senhaRef.set(novaSenha)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { exception -> onFailure(exception) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSenhaScreen(
    navController: NavController,
    idUsuario: String,
    padding: PaddingValues = PaddingValues()
) {
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    // Categorias padrão
//    val categoriasFixas = listOf("Redes Sociais", "Bancos", "Trabalho")

    val categoriaActions = remember { CategoriaActions() }
    val categoriasFirebaseState = remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(idUsuario) {
        categoriaActions.buscarCategoriasDoUsuario(idUsuario) { lista ->
            categoriasFirebaseState.value = lista
        }
    }

//    val categoriasCompletas = categoriasFixas + categoriasFirebaseState.value

    var dropdownExpanded by remember { mutableStateOf(false) }

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
            text = "Cadastrar Senha",
            fontSize = 35.sp,
            color = SuperIDTextWhite
        )

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            label = "Serviço",
            value = nome,
            onValueChange = { nome = it },
            placeholder = "Digite o nome do serviço",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            label = "Senha",
            value = senha,
            onValueChange = { senha = it },
            placeholder = "Digite a senha",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = categoria,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoria") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                categoriasFirebaseState.value.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            categoria = item
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        BtnPrimary(
            label = "Salvar Senha",
            height = 54.dp,
            enabled = nome.isNotBlank() && senha.isNotBlank(),
            onClick = {
                salvarSenha(
                    idUsuario = idUsuario,
                    nome = nome,
                    senha = senha,
                    categoria = categoria.ifBlank { "Todas" },
                    onSuccess = {
                        navController.popBackStack()
                    },
                    onFailure = {
                        Log.e("AddSenhaScreen", "Erro ao salvar senha", it)
                    }
                )
            }
        )
    }
}



