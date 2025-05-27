package com.team43.superidpi3.screen.categoria

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class CategoriaActions {
    private val db = FirebaseFirestore.getInstance()

    fun buscarCategoriasDoUsuario(
        idUsuario: String,
        onResult: (List<String>) -> Unit
    ) {
        db.collection("usuarios")
            .document(idUsuario)
            .collection("categorias")
            .get()
            .addOnSuccessListener { result ->
                val categorias = result.mapNotNull { doc ->
                    try {
                        doc.getString("nome") ?: doc.id
                    } catch (e: Exception) {
                        Log.e("CategoriaParse", "Erro ao converter categoria: ${e.message}")
                        null
                    }
                }
                onResult(categorias)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao buscar categorias: ${e.message}")
                onResult(emptyList())
            }
    }
}
