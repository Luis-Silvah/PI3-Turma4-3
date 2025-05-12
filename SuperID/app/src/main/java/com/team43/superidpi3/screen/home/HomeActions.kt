package com.team43.superidpi3.screen.home

import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.team43.superidpi3.navigation.Routes

class HomeActions(private val ctx: Context, private val navController: NavController) {
    fun buscaUsuario(uid: String, onResult: (Map<String, Any>?) -> Unit) {
        val db = Firebase.firestore
        val TAG = "FIRESTORE-BUSCA"
        val docRef = db.collection("usuarios").document(uid)

        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                Log.d(TAG, "Usuário encontrado: ${document?.data}")
                onResult(document?.data)
            } else {
                Log.e(TAG, "Não foi possivel encontrar usuário", task.exception)
            }
        }
    }

    fun logout() {
        Firebase.auth.signOut()

        navController.navigate(Routes.Welcome) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

}