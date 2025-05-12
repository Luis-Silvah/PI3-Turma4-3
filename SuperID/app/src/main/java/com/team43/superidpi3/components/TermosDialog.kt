package com.team43.superidpi3.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.team43.superidpi3.ui.theme.SuperIDBackground
import com.team43.superidpi3.ui.theme.SuperIDButtonBlue
import com.team43.superidpi3.ui.theme.SuperIDTextWhite

@Composable
fun TermosDialog(show: Boolean,
                 onDismiss: () -> Unit) {
    AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
        TextButton(onClick = onDismiss) {
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