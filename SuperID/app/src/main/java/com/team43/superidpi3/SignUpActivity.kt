package com.team43.superidpi3
//TODO: Deixar em uma linha so sem caixa de texto e alterar pra ao inves de roxo ser azul o contorno da linha
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CadastroUsuario()
        }
    }
}

@RequiresPermission("android.permission.READ_PRIVILEGED_PHONE_STATE")
fun obterIMEI(context: Context): String {
    val gerenciadorTelefonia = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            gerenciadorTelefonia.imei ?: "imei_indisponivel"
        } else {
            @Suppress("DEPRECATION")
            gerenciadorTelefonia.deviceId ?: "imei_indisponivel"
        }
    } else {
        "permissao_negada"
    }
}

fun registrar(ctx: Context, nome: String, email: String, senha: String) {
    val auth = Firebase.auth
    val TAG = "FIREBASE-AUTH"

    // Solicitar permissões se não concedidas
    if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        if (ctx is SignUpActivity) {
            ActivityCompat.requestPermissions(ctx, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
        }
    }

    auth.createUserWithEmailAndPassword(email, senha)
        .addOnCompleteListener @androidx.annotation.RequiresPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") { task ->
            if (task.isSuccessful) {
                val usuario = auth.currentUser
                Log.d(TAG, "Usuário criado com sucesso! ${usuario!!.uid}")

                // Enviar email de verificação
                usuario.sendEmailVerification()
                    .addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            Log.d(TAG, "Email de verificação enviado")
                            Toast.makeText(ctx, "Email de verificação enviado. Verifique sua caixa de entrada.", Toast.LENGTH_LONG).show()
                        } else {
                            Log.e(TAG, "Erro ao enviar email de verificação", verificationTask.exception)
                        }
                    }

                val intent = Intent(ctx, WelcomeActivity::class.java)
                intent.putExtra("uid", usuario.uid)
                intent.putExtra("emailVerificado", false)
                ctx.startActivity(intent)
                if (ctx is SignUpActivity) {
                    ctx.finish()
                }

                val imei = obterIMEI(ctx)
                salvarUsuario(nome, email, usuario.uid, imei, false)
            } else {
                Log.e(TAG, "Não foi possível criar usuário", task.exception)
                Toast.makeText(ctx, "Erro ao criar conta: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
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

// Função para verificar se o email foi verificado
fun verificarEmailVerificado(context: Context, onVerificado: (Boolean) -> Unit) {
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
                    atualizarStatusVerificacao(usuario.uid, true)
                }
                
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

// Função para atualizar o status de verificação no Firestore
fun atualizarStatusVerificacao(uid: String, verificado: Boolean) {
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

// Função para verificar se o usuário pode usar o Login Sem Senha
fun verificarAcessoLoginSemSenha(context: Context, onAcessoPermitido: (Boolean) -> Unit) {
    verificarEmailVerificado(context) { emailVerificado ->
        if (emailVerificado) {
            onAcessoPermitido(true)
        } else {
            // Mostrar diálogo informando sobre a necessidade de verificar o email
            mostrarDialogoVerificacaoEmail(context)
            onAcessoPermitido(false)
        }
    }
}

// Função para mostrar diálogo sobre verificação de email
fun mostrarDialogoVerificacaoEmail(context: Context) {
    // Esta função será implementada na UI
    // Por enquanto, apenas mostra um Toast
    Toast.makeText(
        context,
        "Para usar o Login Sem Senha, você precisa verificar seu email. Verifique sua caixa de entrada.",
        Toast.LENGTH_LONG
    ).show()
}

@Preview(showBackground = true)
@Composable
fun CadastroUsuario() {
    val ctx = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var termosAceitos by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(Color(0xFF2C3E94))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.superid_logo),
            contentDescription = "Logo SuperID",
            modifier = Modifier
                .height(220.dp)
                .width(500.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Criar uma conta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha Mestre") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = termosAceitos,
                onCheckedChange = { termosAceitos = it }
            )
            Text(text = "Li e aceito os ")
            Text(
                text = "termos de uso",
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && termosAceitos) {
                    registrar(ctx, nome, email, senha)
                } else {
                    Toast.makeText(ctx, "Preencha todos os campos e aceite os termos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = termosAceitos
        ) {
            Text(text = "Cadastrar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(text = "Já tem uma conta? ")
            ClickableText(
                text = AnnotatedString("Entrar"),
                onClick = {
                    val intent = Intent(ctx, SignInActivity::class.java)
                    ctx.startActivity(intent)
                },
                style = TextStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)
            )
        }
    }
}
