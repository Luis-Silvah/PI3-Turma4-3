package com.team43.superidpi3.screen.signin

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.team43.superidpi3.navigation.Routes

class SignInActions(private val ctx: Context, private val navController: NavController) {
    fun login(email: String, senha: String) {
        var auth = Firebase.auth
        val TAG = "FIREBASE-AUTH"

        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val usuario = auth.currentUser
                    Log.d(TAG, "Permissão para login, ${usuario!!.uid}")
                    navController.navigate(Routes.home(usuario!!.uid))
                } else {
                    // Log do erro completo para debug
                    Log.e(TAG, "Erro de login: ${task.exception?.javaClass?.simpleName}", task.exception)
                    Log.e(TAG, "Mensagem de erro: ${task.exception?.message}")

                    val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
                    Log.e(TAG, "Código de erro: $errorCode")

                    when (errorCode) {
                        "ERROR_USER_DOES_NOT_EXIST" -> {
                            Toast.makeText(ctx, "Email não encontrado no sistema", Toast.LENGTH_LONG).show()
                        }

                        "ERROR_INVALID_PASSWORD" -> {
                            Toast.makeText(ctx, "Senha incorreta", Toast.LENGTH_LONG).show()
                        }

                        "ERROR_INVALID_EMAIL" -> {
                            Toast.makeText(ctx, "Formato de email inválido", Toast.LENGTH_LONG).show()
                        }

                        "ERROR_NETWORK_REQUEST_FAILED" -> {
                            Toast.makeText(ctx, "Erro de conexão. Verifique sua internet", Toast.LENGTH_LONG).show()
                        }

                        "ERROR_USER_DISABLED" -> {
                            Toast.makeText(ctx, "Conta desativada. Entre em contato com o suporte", Toast.LENGTH_LONG)
                                .show()
                        }

                        else -> {
                            // Se não for nenhum dos erros conhecidos, mostra a mensagem genérica
                            Log.e(TAG, "Erro não tratado: $errorCode")
                            Toast.makeText(ctx, "Erro ao fazer login. Tente novamente mais tarde", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
    }
}