package com.team43.superidpi3.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.team43.superidpi3.ui.theme.SuperIDTextWhite


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("AutoboxingStateCreation", "RememberReturnType")
@Composable
fun HomeScreen(idUsuario: String, navController: NavController, padding: PaddingValues) {


    Column(
        modifier = Modifier
            .padding(padding),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Listagem de senhas", color = SuperIDTextWhite, fontSize = 18.sp)
    }

}
