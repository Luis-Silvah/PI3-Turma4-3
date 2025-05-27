package com.team43.superidpi3.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team43.superidpi3.ui.theme.SuperIDGrayPrimary
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import com.team43.superidpi3.ui.theme.SuperIDWhite


@Composable
fun SenhaCard(title: String,
              navController: NavController,
              description: String,
              senhaId:String,
              password: String) {
    var expanded by remember { mutableStateOf(false) }
    var senhaVisivel by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = SuperIDGrayPrimary,
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Ícone do app
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF0066FF), shape = RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Título e descrição
            // Título, descrição e senha
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top) // Garante alinhamento vertical com o topo do ícone
            ) {
                Text(text = title, color = SuperIDTextWhite)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    color = SuperIDTextWhite.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.Start) // Alinha com o início do título/descrição
                ) {
                    IconButton(
                        onClick = { senhaVisivel = !senhaVisivel },
                        modifier = Modifier.size(20.dp).padding(end = 0.dp) // Reduz tamanho e remove espaçamento
                    ) {
                        Icon(
                            imageVector = if (senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (senhaVisivel) "Ocultar senha" else "Exibir senha",
                            tint = SuperIDTextWhite.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = if (senhaVisivel) password else "●●●●●●●●●",
                        color = SuperIDTextWhite.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }


            // Menu de três pontinhos
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = SuperIDTextWhite)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = SuperIDWhite
                ) {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                        },
                        text = {
                            Text("Editar")
                        })
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            navController.navigate("delete_senha/$senhaId")

                        },
                        text = {
                            Text("Deletar")
                        })
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                        },
                        text = {
                            Text("Ler Qrcode")
                        })
                }
            }
        }
    }
}

