package com.team43.superidpi3.screen.categoria

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team43.superidpi3.ui.theme.SuperIDTextWhite

@Composable
fun CategoriaScreen(navController: NavController, padding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(padding),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Listagem de categiruas", color = SuperIDTextWhite, fontSize = 18.sp)
    }
}