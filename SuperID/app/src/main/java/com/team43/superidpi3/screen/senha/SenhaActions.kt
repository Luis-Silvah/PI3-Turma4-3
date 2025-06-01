package com.team43.superidpi3.screen.senha

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.team43.superidpi3.domain.Senha
import com.team43.superidpi3.utils.Criptografia
import kotlinx.coroutines.tasks.await

class SenhaActions {
    private val db = FirebaseFirestore.getInstance()
    fun salvarSenha(
        idUsuario: String,
        nome: String,
        senha: String,
        nomeCategoria: String,
        descricao: String?,
        login: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        val senhaCriptografada = Criptografia.encrypt(senha)

        db.collection("usuarios")
            .document(idUsuario)
            .collection("categorias")
            .whereEqualTo("nome", nomeCategoria)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val categoriaDoc = querySnapshot.documents.first()
                    val categoriaId = categoriaDoc.id

                    val senhaRef = db.collection("usuarios")
                        .document(idUsuario)
                        .collection("categorias")
                        .document(categoriaId)
                        .collection("senhas")
                        .document()

                    val novaSenha = hashMapOf(
                        "id" to senhaRef.id,
                        "nome" to nome,
                        "senha" to senhaCriptografada,
                        "categoria" to nomeCategoria,
                        "descricao" to (descricao ?: ""),
                        "login" to (login ?: "")
                    )

                    senhaRef.set(novaSenha)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { exception -> onFailure(exception) }

                } else {
                    onFailure(Exception("Categoria não encontrada."))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
    fun deletarSenha(
        firestore: FirebaseFirestore,
        userId: String,
        categoriaNome: String,
        senhaId: String,
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
                        .collection("senhas")
                        .document(senhaId)
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
    fun editarSenha(
        userId: String,
        categoriaNome: String,
        senhaId: String,
        novosDados: Map<String, String>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios")
            .document(userId)
            .collection("categorias")
            .whereEqualTo("nome", categoriaNome)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val categoriaDoc = querySnapshot.documents[0]
                    val categoriaId = categoriaDoc.id

                    val senhaRef = db.collection("usuarios")
                        .document(userId)
                        .collection("categorias")
                        .document(categoriaId)
                        .collection("senhas")
                        .document(senhaId)

                    senhaRef.update(novosDados as Map<String, Any>)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onFailure(it) }
                } else {
                    onFailure(Exception("Categoria não encontrada."))
                }
            }
            .addOnFailureListener { onFailure(it) }
    }



    fun buscarSenhasDoUsuario(
        idUsuario: String,
        onResult: (List<Senha>) -> Unit
    ) {
        db.collection("usuarios")
            .document(idUsuario)
            .collection("categorias")
            .get()
            .addOnSuccessListener { categoriasSnapshot ->

                if (categoriasSnapshot.isEmpty) {
                    onResult(emptyList())
                    return@addOnSuccessListener
                }

                val senhasTotais = mutableListOf<Senha>()
                var categoriasProcessadas = 0

                categoriasSnapshot.documents.forEach { categoriaDoc ->
                    val categoriaNome = categoriaDoc.getString("nome") ?: "Outros"
                    val categoriaId = categoriaDoc.id

                    db.collection("usuarios")
                        .document(idUsuario)
                        .collection("categorias")
                        .document(categoriaId)
                        .collection("senhas")
                        .get()
                        .addOnSuccessListener { senhasSnapshot ->

                            val senhasCategoria = senhasSnapshot.mapNotNull { doc ->
                                try {
                                    Senha(
                                        id = doc.getString("id") ?: doc.id,
                                        nome = doc.getString("nome") ?: "",
                                        senha = doc.getString("senha") ?: "",
                                        categoria = categoriaNome,
                                        descricao = doc.getString("descricao") ?: "",
                                        login = doc.getString("login") ?: ""
                                    )
                                } catch (e: Exception) {
                                    Log.e("SenhaParse", "Erro ao converter senha: ${e.message}")
                                    null
                                }
                            }
                            senhasTotais.addAll(senhasCategoria)

                            categoriasProcessadas++
                            if (categoriasProcessadas == categoriasSnapshot.size()) {
                                // Terminou de buscar todas as categorias e senhas
                                onResult(senhasTotais)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Erro ao buscar senhas na categoria $categoriaNome: ${e.message}")
                            categoriasProcessadas++
                            if (categoriasProcessadas == categoriasSnapshot.size()) {
                                onResult(senhasTotais)
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao buscar categorias: ${e.message}")
                onResult(emptyList())
            }
    }
}
