package com.team43.superidpi3.screen.signin.navigation

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ForgotPasswordActions(private val ctx: Context) {
    fun verificarEmailNoFirestore(
        email: String,
        onResult: (existe: Boolean, verificado: Boolean) -> Unit
    ) {
        val db = Firebase.firestore
        db.collection("usuarios")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val doc = documents.first()
                    val verificado = doc.getBoolean("emailVerificado") ?: false
                    onResult(true, verificado)
                } else {
                    onResult(false, false)
                }
            }
            .addOnFailureListener {
                onResult(false, false)
            }
    }

    fun recuperarSenha(email: String, navigate: () -> Unit) {
        val auth = Firebase.auth

        verificarEmailNoFirestore(email) { existe, verificado ->
            when {
                !existe -> {
//                    Toast.makeText(ctx, "Email não encontrado", Toast.LENGTH_LONG).show()
                }
                !verificado -> {
//                    Toast.makeText(ctx, "Por favor, verifique seu email antes de solicitar a recuperação de senha", Toast.LENGTH_LONG).show()
                }
                else -> {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
//                        Toast.makeText(
//                            ctx,
//                            "E-mail de recuperação enviado com sucesso.",
//                            Toast.LENGTH_LONG
//                        ).show()
                        navigate()
                    }
                }
            }
        }
    }

}