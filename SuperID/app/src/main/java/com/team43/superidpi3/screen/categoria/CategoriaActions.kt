package com.team43.superidpi3.screen.categoria

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class CategoriaActions {
    private val db = FirebaseFirestore.getInstance()

    fun buscarCategoriasDoUsuario(
        idUsuario: String,
        onResult: (List<String>) -> Unit
    ) {
            val userCategoriasRef = db.collection("usuarios").document(idUsuario).collection("categorias")
            val categoriasPadraoRef = db.collection("categorias")

            userCategoriasRef.get().addOnSuccessListener { userResult ->
                val userCategorias = userResult.mapNotNull { doc ->
                    try {
                        doc.getString("nome") ?: doc.id
                    } catch (e: Exception) {
                        Log.e("CategoriaParse", "Erro ao converter categoria: ${e.message}")
                        null
                    }
                }

                val userCategoriaNomes = userCategorias.toSet()

                // Filtro categorias usuario
                categoriasPadraoRef.get().addOnSuccessListener { padraoResult ->
                    val categoriasPadrao = padraoResult.mapNotNull { doc ->
                        try {
                            val nome = doc.getString("nome") ?: return@mapNotNull null
                            if (nome !in userCategoriaNomes) {
                                nome
                            } else {
                                null
                            }
                        } catch (e: Exception) {
                            Log.e("CategoriaPadraoParse", "Erro ao converter categoria padrão: ${e.message}")
                            null
                        }
                    }

                    val todas = userCategorias + categoriasPadrao
                    onResult(todas)
                }.addOnFailureListener {
                    onResult(userCategorias)
                }
            }.addOnFailureListener {
                onResult(emptyList())
            }


//        db.collection("usuarios")
//            .document(idUsuario)
//            .collection("categorias")
//            .get()
//            .addOnSuccessListener { result ->
//                val categorias = result.mapNotNull { doc ->
//                    try {
//                        doc.getString("nome") ?: doc.id
//                    } catch (e: Exception) {
//                        Log.e("CategoriaParse", "Erro ao converter categoria: ${e.message}")
//                        null
//                    }
//                }
//                onResult(categorias)
//            }
//            .addOnFailureListener { e ->
//                Log.e("Firestore", "Erro ao buscar categorias: ${e.message}")
//                onResult(emptyList())
//            }
    }
}
