package com.team43.superidpi3.screen.senha

import android.util.Log
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
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.components.Header
import com.team43.superidpi3.components.InputField
import com.team43.superidpi3.domain.Senha
import com.team43.superidpi3.screen.categoria.CategoriaActions
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import com.team43.superidpi3.utils.Criptografia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSenhaScreen(
    navController: NavController,
    padding: PaddingValues,
    senhaId: String,
    categoriaNome: String
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val senhaActions = remember { SenhaActions() }
    val categoriaActions = remember { CategoriaActions() }
    val firestore = FirebaseFirestore.getInstance()

    var categoriaId by remember { mutableStateOf<String?>(null) }
    var senhaOriginal by remember { mutableStateOf<Senha?>(null) }
    var categoriasFirebase by remember { mutableStateOf(listOf<String>()) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        categoriaActions.buscarCategoriasDoUsuario(userId) {
            categoriasFirebase = it
        }
    }

    LaunchedEffect(Unit) {
        firestore.collection("usuarios")
            .document(userId)
            .collection("categorias")
            .get()
            .addOnSuccessListener { result ->
                val categoriaDoc = result.firstOrNull { it.getString("nome") == categoriaNome }
                categoriaDoc?.let {
                    categoriaId = it.id
                } ?: Log.e("EditSenhaScreen", "Categoria '$categoriaNome' não encontrada.")
            }
            .addOnFailureListener {
                Log.e("EditSenhaScreen", "Erro ao buscar categorias", it)
            }
    }

    LaunchedEffect(categoriaId) {
        categoriaId?.let { catId ->
            firestore.collection("usuarios")
                .document(userId)
                .collection("categorias")
                .document(catId)
                .collection("senhas")
                .document(senhaId)
                .get()
                .addOnSuccessListener { document ->
                    document?.toObject(Senha::class.java)?.let {
                        senhaOriginal = it.copy(id = document.id, categoria = categoriaNome)
                    }
                }
                .addOnFailureListener {
                    Log.e("EditSenhaScreen", "Erro ao buscar senha", it)
                }
        }
    }

    senhaOriginal?.let { senha ->
        var nome by remember { mutableStateOf(senha.nome) }
        var senhaTexto by remember { mutableStateOf("") }
        var login by remember { mutableStateOf(senha.login ?: "") }
        var descricao by remember { mutableStateOf(senha.descricao ?: "") }
        var categoria by remember { mutableStateOf(senha.categoria) }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Header { navController.popBackStack() }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Editar Senha", color = SuperIDTextWhite, fontSize = 35.sp)
            Spacer(modifier = Modifier.height(16.dp))

            InputField("Nome", nome, { nome = it }, "Nome da senha")
            Spacer(modifier = Modifier.height(16.dp))

            InputField("Login", login, { login = it }, "Novo Login")
            Spacer(modifier = Modifier.height(16.dp))

            InputField("Senha", senhaTexto, { senhaTexto = it }, "Nova senha")
            Spacer(modifier = Modifier.height(16.dp))

            InputField("Descrição", descricao, { descricao = it }, "Nova Descrição")
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
                    categoriasFirebase.forEach { item ->
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
                label = "Salvar Alterações",
                height = 54.dp,
                enabled = true,
                onClick = {
                    if (categoria != senha.categoria) {
                        senhaActions.deletarSenha(
                            firestore = firestore,
                            userId = userId,
                            categoriaNome = senha.categoria,
                            senhaId = senha.id,
                            onSuccess = {
                                senhaActions.salvarSenha(
                                    idUsuario = userId,
                                    nome = nome,
                                    senha = if (senhaTexto.isNotEmpty()) senhaTexto else Criptografia.decrypt(senha.senha),
                                    nomeCategoria = categoria,
                                    descricao = descricao,
                                    login = login,
                                    onSuccess = { navController.popBackStack() },
                                    onFailure = {
                                        Log.e("SalvarSenha", it.message ?: "Erro ao salvar nova senha")
                                    }
                                )
                            },
                            onError = {
                                Log.e("DeletarSenha", it.message ?: "Erro ao deletar senha")
                            }
                        )
                    } else {
                        senhaActions.editarSenha(
                            userId = userId,
                            categoriaNome = categoria,
                            senhaId = senha.id,
                            novosDados = mapOf(
                                "nome" to nome,
                                "senha" to if (senhaTexto.isNotEmpty()) Criptografia.encrypt(senhaTexto) else senha.senha,
                                "login" to login,
                                "descricao" to descricao,
                                "categoria" to categoria
                            ),
                            onSuccess = { navController.popBackStack() },
                            onFailure = {
                                Log.e("EditarSenha", it.message ?: "Erro ao editar senha")
                            }
                        )
                    }
                }
            )
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}
