package com.team43.superidpi3.screen.senha

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.team43.superidpi3.domain.Senha
import kotlinx.coroutines.tasks.await

class SenhaActions {
    private val db = FirebaseFirestore.getInstance()

    // Usando callbacks normais, mas com múltiplas chamadas assíncronas encadeadas
    fun buscarSenhasDoUsuario(
        idUsuario: String,
        onResult: (List<Senha>) -> Unit
    ) {
        // Primeiro buscar categorias do usuário
        db.collection("usuarios")
            .document(idUsuario)
            .collection("categorias")
            .get()
            .addOnSuccessListener { categoriasSnapshot ->

                if (categoriasSnapshot.isEmpty) {
                    // Sem categorias, retorna lista vazia
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
