package com.team43.superidpi3.screen.signup

import android.content.Context
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team43.superidpi3.navigation.Routes

class SignUpActions(private val ctx: Context, private val navController: NavController) {

    fun registrar(nome: String, email: String, senha: String) {
        val auth = Firebase.auth
        val TAG = "FIREBASE-AUTH"

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val usuario = auth.currentUser
                    Log.d(TAG, "Usuário criado com sucesso! ${usuario!!.uid}")

                    // Enviar email de verificação
                    usuario.sendEmailVerification()
                        .addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                Log.d(TAG, "Email de verificação enviado")
//                                Toast.makeText(ctx, "Email de verificação enviado. Verifique sua caixa de entrada.", Toast.LENGTH_LONG).show()
                            } else {
                                Log.e(TAG, "Erro ao enviar email de verificação", verificationTask.exception)
                            }
                        }

                    val androidId = Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
                    Log.d("ANDROID_ID", androidId)

                    salvarUsuario(nome, email, usuario.uid, androidId, false)

                    navController.navigate(Routes.home(usuario.uid))

                } else {
                    val errorCode = (task.exception as? com.google.firebase.auth.FirebaseAuthException)?.errorCode
                    Log.e(TAG, "Código de erro: $errorCode")
                    //tramento de erros
                    val toast = when (errorCode) {
                        "ERROR_WEAK_PASSWORD" -> "Erro: senha fraca. A senha deve conter pelo menos 6 caracteres"
                        "ERROR_INVALID_EMAIL" -> "Formato de email inválido"
                        "ERROR_NETWORK_REQUEST_FAILED" -> "Erro de conexão. Verifique sua internet"
                        "ERROR_USER_DISABLED" -> "Conta desativada. Entre em contato com o suporte"
                        else -> "Não foi possível criar usuário"
                    }
                    Toast.makeText(ctx, toast, Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Erro ao criar conta: ${task.exception?.localizedMessage}")
                }
            }
    }

    fun salvarUsuario(nome: String, email: String, uid: String, imei: String, emailVerificado: Boolean) {
        val db = Firebase.firestore
        val TAG = "FIREBASE-FIRESTORE"

        val usuario = hashMapOf(
            "nome" to nome,
            "email" to email,
            "uid" to uid,
            "imei" to imei,
            "emailVerificado" to emailVerificado
        )

        db.collection("usuarios")
            .document(uid)
            .set(usuario)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Usuário salvo com sucesso!")
                } else {
                    Log.e(TAG, "Não foi possível salvar usuário", task.exception)
                }
            }
    }

    // Função para verificar se o usuário pode usar o Login Sem Senha
//    fun verificarAcessoLoginSemSenha(onAcessoPermitido: (Boolean) -> Unit) {
//        verificarEmailVerificado() { emailVerificado ->
//            if (emailVerificado) {
//                onAcessoPermitido(true)
//            } else {
//                // Mostrar diálogo informando sobre a necessidade de verificar o email
//                mostrarDialogoVerificacaoEmail()
//                onAcessoPermitido(false)
//            }
//        }
//    }

    // Função para mostrar diálogo sobre verificação de email
    fun mostrarDialogoVerificacaoEmail() {
        // Esta função será implementada na UI
        // Por enquanto, apenas mostra um Toast
        Toast.makeText(
            ctx,
            "Para usar o Login Sem Senha, você precisa verificar seu email. Verifique sua caixa de entrada.",
            Toast.LENGTH_LONG
        ).show()
    }
}