package com.team43.superidpi3.screen.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.team43.superidpi3.navigation.Routes

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(modifier = Modifier.padding(24.dp)) {

        Button(onClick = { navController.navigate(Routes.SignUp)}) {
            Text("Cadastrar")
        }

        Button(onClick = { navController.navigate(Routes.SignIn)}) {
            Text("Entrar")
        }
    }
}