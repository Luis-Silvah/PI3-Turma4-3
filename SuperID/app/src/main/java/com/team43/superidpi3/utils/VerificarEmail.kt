package com.team43.superidpi3.utils

import android.content.Context
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VerificarEmail(private val ctx: Context) {
    fun verifica(onVerificado: (Boolean) -> Unit) {
        val auth = Firebase.auth
        val TAG = "VERIFICACAO-EMAIL"

        val usuario = auth.currentUser
        if (usuario != null) {
            usuario.reload().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val emailVerificado = usuario.isEmailVerified
                    Log.d(TAG, "Email verificado: $emailVerificado")

                    // Atualizar o status no Firestore
                    if (emailVerificado) {
                        atualizaStatus(usuario.uid, true)
                    }
//                    else {
//                        Toast.makeText(ctx, "Seu email ainda não foi verificado.", Toast.LENGTH_SHORT).show()
//                    }

                    onVerificado(emailVerificado)
                } else {
                    Log.e(TAG, "Erro ao recarregar usuário", task.exception)
                    onVerificado(false)
                }
            }
        } else {
            Log.e(TAG, "Usuário não está logado")
            onVerificado(false)
        }
    }

    private fun atualizaStatus(uid: String, verificado: Boolean) {
        val db = Firebase.firestore
        val TAG = "ATUALIZACAO-VERIFICACAO"

        db.collection("usuarios")
            .document(uid)
            .update("emailVerificado", verificado)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Status de verificação atualizado com sucesso")
                } else {
                    Log.e(TAG, "Erro ao atualizar status de verificação", task.exception)
                }
            }
    }
}

