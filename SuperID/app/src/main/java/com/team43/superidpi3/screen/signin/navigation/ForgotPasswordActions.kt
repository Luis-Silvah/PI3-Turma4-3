package com.team43.superidpi3.screen.signin.navigation

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team43.superidpi3.navigation.Routes

class ForgotPasswordActions(private val ctx: Context, private val navController: NavController) {
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

    fun recuperarSenha(email: String) {
        val auth = Firebase.auth
        val usuarioAuth = auth.currentUser
        val idUsuario = usuarioAuth?.uid ?: "nologin"

        verificarEmailNoFirestore(email) { existe, verificado ->
            when {
                !existe -> {
                    navController.navigate(Routes.emailSentFail(idUsuario))
                }
                !verificado -> {
                    navController.navigate(Routes.emailSentFail(idUsuario))
                }
                else -> {
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener {
                            navController.navigate(Routes.emailSentSuccess(idUsuario))
                        }
                }
            }
        }
    }
}