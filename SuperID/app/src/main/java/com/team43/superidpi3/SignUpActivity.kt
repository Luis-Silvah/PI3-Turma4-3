package com.team43.superidpi3
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
import androidx.compose.ui.res.colorResource
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
import com.team43.superidpi3.ui.theme.SuperIDBlue
import com.team43.superidpi3.ui.theme.SuperIDWhite
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.team43.superidpi3.ui.theme.SuperIDBackground
import com.team43.superidpi3.ui.theme.SuperIDButtonBlue
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff


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
        .addOnCompleteListener { task ->
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
                val errorCode = (task.exception as? com.google.firebase.auth.FirebaseAuthException)?.errorCode
                Log.e(TAG, "Código de erro: $errorCode")
                //tramento de erros
                when (errorCode) {
                    "ERROR_WEAK_PASSWORD" -> {
                        Toast.makeText(ctx, "Erro: senha fraca. A senha deve conter pelo menos 6 caracteres", Toast.LENGTH_LONG).show()
                    }

                    "ERROR_INVALID_EMAIL" -> {
                        Toast.makeText(ctx, "Formato de email inválido", Toast.LENGTH_LONG).show()
                    }
                    "ERROR_NETWORK_REQUEST_FAILED" -> {
                        Toast.makeText(ctx, "Erro de conexão. Verifique sua internet", Toast.LENGTH_LONG).show()
                    }
                    "ERROR_USER_DISABLED" -> {
                        Toast.makeText(ctx, "Conta desativada. Entre em contato com o suporte", Toast.LENGTH_LONG).show()
                    }
                    else -> {

                        Log.e(TAG, "Não foi possível criar usuário", task.exception)
                        Toast.makeText(ctx, "Erro ao criar conta: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
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
    var showTermsDialog by remember { mutableStateOf(false) }
    var senhaVisivel by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SuperIDBackground)
            .padding(horizontal = 24.dp, vertical = 0.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val intent = Intent(ctx, MainActivity::class.java)
                    ctx.startActivity(intent)
                },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = SuperIDTextWhite.copy(alpha = 0.05f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = SuperIDTextWhite,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.superid_logo),
                contentDescription = "Logo SuperID",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
        }
        Text(
            text = "Cadastrar Conta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = SuperIDTextWhite,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        // Nome
        Text(
            text = "Nome",
            color = SuperIDTextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = nome,
            onValueChange = { nome = it },
            placeholder = { Text("Digite seu nome", color = SuperIDTextWhite.copy(alpha = 0.7f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SuperIDBackground,
                unfocusedContainerColor = SuperIDBackground,
                focusedIndicatorColor = SuperIDButtonBlue,
                unfocusedIndicatorColor = SuperIDTextWhite.copy(alpha = 0.2f),
                focusedTextColor = SuperIDTextWhite,
                unfocusedTextColor = SuperIDTextWhite,
                cursorColor = SuperIDTextWhite,
                focusedLabelColor = SuperIDTextWhite,
                unfocusedLabelColor = SuperIDTextWhite
            )
        )
        Spacer(modifier = Modifier.height(18.dp))
        // Email
        Text(
            text = "Email",
            color = SuperIDTextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("example@superid.com", color = SuperIDTextWhite.copy(alpha = 0.7f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = null, tint = SuperIDTextWhite.copy(alpha = 0.7f))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SuperIDBackground,
                unfocusedContainerColor = SuperIDBackground,
                focusedIndicatorColor = SuperIDButtonBlue,
                unfocusedIndicatorColor = SuperIDTextWhite.copy(alpha = 0.2f),
                focusedTextColor = SuperIDTextWhite,
                unfocusedTextColor = SuperIDTextWhite,
                cursorColor = SuperIDTextWhite,
                focusedLabelColor = SuperIDTextWhite,
                unfocusedLabelColor = SuperIDTextWhite
            )
        )
        Spacer(modifier = Modifier.height(18.dp))
        // Senha
        Text(
            text = "Senha",
            color = SuperIDTextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        TextField(
            value = senha,
            onValueChange = { senha = it },
            placeholder = { Text("Digite sua senha", color = SuperIDTextWhite.copy(alpha = 0.7f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = null, tint = SuperIDTextWhite.copy(alpha = 0.7f))
            },
            trailingIcon = {
                val image = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (senhaVisivel) "Ocultar senha" else "Mostrar senha"
                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                    Icon(imageVector = image, contentDescription = description, tint = SuperIDTextWhite.copy(alpha = 0.7f))
                }
            },
            visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SuperIDBackground,
                unfocusedContainerColor = SuperIDBackground,
                focusedIndicatorColor = SuperIDButtonBlue,
                unfocusedIndicatorColor = SuperIDTextWhite.copy(alpha = 0.2f),
                focusedTextColor = SuperIDTextWhite,
                unfocusedTextColor = SuperIDTextWhite,
                cursorColor = SuperIDTextWhite,
                focusedLabelColor = SuperIDTextWhite,
                unfocusedLabelColor = SuperIDTextWhite
            )
        )
        Spacer(modifier = Modifier.height(18.dp))
        // Checkbox e termos com links
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Checkbox(
                checked = termosAceitos,
                onCheckedChange = { termosAceitos = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = SuperIDButtonBlue,
                    uncheckedColor = SuperIDTextWhite,
                    checkmarkColor = SuperIDTextWhite
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            val annotatedString = buildAnnotatedString {
                withStyle(SpanStyle(color = SuperIDTextWhite)) {
                    append("Você aceita os ")
                }
                pushStringAnnotation(tag = "termos", annotation = "termos")
                withStyle(SpanStyle(color = SuperIDButtonBlue, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Medium)) {
                    append("Termos de uso e reconhece a declaração de privacidade e política de cookies")
                }
                pop()
            }
            ClickableText(
                text = annotatedString,
                style = TextStyle(fontSize = 12.sp),
                onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "termos", start = offset, end = offset)
                        .firstOrNull()?.let {
                            showTermsDialog = true
                        }
                }
            )
        }
        if (showTermsDialog) {
            AlertDialog(
                onDismissRequest = { showTermsDialog = false },
                confirmButton = {
                    TextButton(onClick = { showTermsDialog = false }) {
                        Text("Fechar", color = SuperIDButtonBlue)
                    }
                },
                title = {
                    Text("Termos de Uso – SuperID", color = SuperIDTextWhite, fontWeight = FontWeight.Bold)
                },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            "Última atualização: 25/04/2025\n\n" +
                            "Bem-vindo ao SuperID! Estes Termos de Uso regulam o acesso e uso do nosso serviço. Ao utilizar o SuperID, você concorda com estes termos. Leia-os com atenção.\n\n" +
                            "1. Sobre o SuperID\nO SuperID é um serviço de identidade digital e autenticação segura que permite que você se identifique em plataformas parceiras de forma rápida, confiável e protegida.\n\n" +
                            "2. Uso do Serviço\n• Você deve ter pelo menos 18 anos ou ser autorizado por um responsável legal para utilizar o SuperID.\n• Você é responsável por manter suas credenciais seguras. Não compartilhe seu acesso com terceiros.\n• É proibido usar o SuperID para fins ilegais ou não autorizados.\n\n" +
                            "3. Privacidade e Dados\nLevamos sua privacidade a sério. Os dados fornecidos ao SuperID são tratados conforme nossa Política de Privacidade.\n• Utilizamos seus dados apenas para fins de autenticação e identificação.\n• Não vendemos seus dados a terceiros.\n• Você pode solicitar a exclusão dos seus dados a qualquer momento.\n\n" +
                            "4. Responsabilidades\n• O SuperID se compromete a manter o serviço disponível e seguro, mas não garante ausência de falhas ou interrupções.\n• Não nos responsabilizamos por danos decorrentes de uso indevido ou falhas de terceiros.\n\n" +
                            "5. Modificações nos Termos\nPodemos alterar estes Termos de Uso periodicamente. Se fizermos mudanças relevantes, notificaremos você.\n\n" +
                            "6. Cancelamento e Encerramento\nVocê pode encerrar seu uso do SuperID a qualquer momento. Também podemos encerrar seu acesso em caso de violação destes termos.\n\n" +
                            "7. Contato\nEm caso de dúvidas, entre em contato pelo e-mail: suporte@superid.com",
                            color = SuperIDTextWhite,
                            fontSize = 14.sp
                        )
                    }
                },
                containerColor = SuperIDBackground
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        // Botão
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
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SuperIDButtonBlue,
                disabledContainerColor = SuperIDButtonBlue.copy(alpha = 0.3f),
                contentColor = SuperIDTextWhite,
                disabledContentColor = SuperIDTextWhite.copy(alpha = 0.5f)
            ),
            shape = MaterialTheme.shapes.medium,
            enabled = termosAceitos
        ) {
            Text(text = "Criar conta", color = SuperIDTextWhite, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        // Link para login
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Já tem uma conta? ", color = SuperIDTextWhite, fontSize = 14.sp)
            ClickableText(
                text = AnnotatedString("Entrar"),
                onClick = {
                    val intent = Intent(ctx, SignInActivity::class.java)
                    ctx.startActivity(intent)
                },
                style = TextStyle(color = SuperIDButtonBlue, textDecoration = TextDecoration.Underline, fontSize = 14.sp)
            )
        }
    }
}

