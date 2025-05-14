package com.team43.superidpi3.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import com.team43.superidpi3.ui.theme.SuperIDGrayPrimary
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import com.team43.superidpi3.ui.theme.SuperIDWhite


@Composable
fun SenhaCard(title: String, description: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = SuperIDGrayPrimary,
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 16.dp)
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
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = SuperIDTextWhite)
                Text(
                    text = description,
                    color = SuperIDTextWhite.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = SuperIDTextWhite.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "●●●●●●●●●●●", color = SuperIDTextWhite.copy(alpha = 0.7f), fontSize = 14.sp)
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
