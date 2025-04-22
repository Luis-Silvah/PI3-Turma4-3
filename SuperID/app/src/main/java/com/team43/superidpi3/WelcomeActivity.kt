package com.team43.superidpi3


import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Welcome()
        }

    }
}

fun buscaUsuario(uid: String, onResult: (Map<String, Any>?) -> Unit) {
    var db = Firebase.firestore
    val TAG = "FIRESTORE-BUSCA"
    val docRef = db.collection("usuarios").document(uid)

    docRef.get().addOnCompleteListener { task ->
        if(task.isSuccessful) {
            val document = task.result
            Log.d(TAG, "Usuário encontrado: ${document?.data}")
            onResult(document?.data)
        } else {
            Log.e(TAG, "Não foi possivel encontrar usuário", task.exception)
        }
    }


}

@Composable
fun Welcome() {
    val context = LocalContext.current
    val uid = remember {
        (context as? Activity)?.intent?.getStringExtra("uid")
    }
    val usuarioState = remember { mutableStateOf<Map<String, Any>?>(null) }

    if (!uid.isNullOrEmpty()) {
        buscaUsuario(uid, { dados ->
            usuarioState.value = dados
        })
    } else {
        Log.e("uid", "UID não encontrado")
    }

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Bem-vindo ao programa experimental Firebase")
//        Button(onClick = {
//            if (!uid.isNullOrEmpty()) {
//                buscaUsuario(uid, { dados ->
//                    usuarioState.value = dados
//                })
//            } else {
//                Log.e("uid", "UID não encontrado")
//            }
//        }) {
//            Text("Buscar Usuário")
//        }

        usuarioState.value?.let { usuario ->
            Text("Nome: ${usuario["nome"] ?: "Não informado"}")
            Text("Email: ${usuario["email"] ?: "Não informado"}")
        }
    }
}