package com.team43.superidpi3.screen.categoria

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class CategoriaActions {
    private val db = FirebaseFirestore.getInstance()
    fun buscarCategoriasComPadrao(idUsuario: String, onResult: (List<CategoriaItem>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(idUsuario).collection("categorias")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    try {
                        CategoriaItem(
                            nome = doc.getString("nome") ?: "",
                            isPadrao = doc.getBoolean("isPadrao") ?: false
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                onResult(lista)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
    fun buscarCategoriasDoUsuario(
        idUsuario: String,
        onResult: (List<String>) -> Unit
    ) {
        val userCategoriasRef = db.collection("usuarios").document(idUsuario).collection("categorias")

        userCategoriasRef.get()
            .addOnSuccessListener { userResult ->
                val userCategorias = userResult.mapNotNull { doc ->
                    try {
                        doc.getString("nome") ?: doc.id
                    } catch (e: Exception) {
                        Log.e("CategoriaParse", "Erro ao converter categoria: ${e.message}")
                        null
                    }
                }
                onResult(userCategorias)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

}
