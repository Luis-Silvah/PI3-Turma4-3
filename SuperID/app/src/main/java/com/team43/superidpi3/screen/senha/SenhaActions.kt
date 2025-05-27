package com.team43.superidpi3.screen.senha

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.team43.superidpi3.domain.Senha

class SenhaActions {
    private val db = FirebaseFirestore.getInstance()

    fun buscarSenhasDoUsuario(
        idUsuario: String,
        onResult: (List<Senha>) -> Unit
    ) {
        db.collection("usuarios")
            .document(idUsuario)
            .collection("senhas")
            .get()
            .addOnSuccessListener { result ->
                val senhas = result.mapNotNull { doc ->
                    try {
                        Senha(
                            id = doc.getString("id") ?: doc.id,
                            nome = doc.getString("nome") ?: "",
                            senha = doc.getString("senha") ?: "",
                            categoria = doc.getString("categoria") ?: "Outros"
                        )
                    } catch (e: Exception) {
                        Log.e("SenhaParse", "Erro ao converter senha: ${e.message}")
                        null
                    }
                }
                onResult(senhas)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao buscar senhas: ${e.message}")
                onResult(emptyList())
            }
    }
}
