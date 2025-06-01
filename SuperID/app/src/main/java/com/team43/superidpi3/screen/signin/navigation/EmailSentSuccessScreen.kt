package com.team43.superidpi3.screen.signin.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.team43.superidpi3.components.Header
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.team43.superidpi3.R
import androidx.compose.foundation.shape.CircleShape

@Composable
fun EmailSentSuccessScreen(navController: NavController, idUsuario: String) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.superid_logo),
                    contentDescription = "Logo SuperID",
                    modifier = Modifier
                        .height(140.dp)
                        .width(140.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Enviado",
                    tint = SuperIDButtonBlue,
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Email Enviado",
                color = SuperIDTextWhite,
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Verifique sua caixa de entrada",
                color = Color(0xFFBDBDBD),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            BtnPrimary(
                label = "Voltar",
                height = 54.dp,
                enabled = true,
                onClick = { navController.navigate(Routes.profile(idUsuario)) },
                containerColor = SuperIDButtonBlue
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailSentSuccessScreenPreview() {
    EmailSentSuccessScreen(navController = rememberNavController(), idUsuario = "")
} 