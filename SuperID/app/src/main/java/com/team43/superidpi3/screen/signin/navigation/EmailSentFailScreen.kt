package com.team43.superidpi3.screen.signin.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.navigation.Routes
import com.team43.superidpi3.ui.theme.SuperIDButtonBlue
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import com.team43.superidpi3.ui.theme.SuperIDBackground
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.team43.superidpi3.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.team43.superidpi3.components.Header

@Composable
fun EmailSentFailScreen(navController: NavController, idUsuario: String) {
    val usuarioAuth = Firebase.auth.currentUser
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SuperIDBackground)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Header { navController.popBackStack() }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.Red, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Erro",
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Email não validado",
                color = SuperIDTextWhite,
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Você precisa validar seu email para usar esse recurso! Clique em validar email e tente novamente.",
                color = Color(0xFFBDBDBD),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 48.dp),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            BtnPrimary(
                label = "Validar Email",
                height = 54.dp,
                enabled = true,
                onClick = {
                    usuarioAuth?.sendEmailVerification()
                },
                containerColor = SuperIDButtonBlue
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailSentFailScreenPreview() {
    EmailSentFailScreen(navController = rememberNavController(), idUsuario = "")
} 