package com.team43.superidpi3.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.team43.superidpi3.ui.theme.SuperIDButtonBlue
import com.team43.superidpi3.ui.theme.SuperIDTextWhite

@Composable
fun BtnPrimary(label: String, height: Dp, enabled: Boolean, onClick: () -> Unit){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        colors = ButtonDefaults.buttonColors(
            containerColor = SuperIDButtonBlue,
            disabledContainerColor = SuperIDButtonBlue.copy(alpha = 0.3f),
            contentColor = SuperIDTextWhite,
            disabledContentColor = SuperIDTextWhite.copy(alpha = 0.5f)
        ),
        shape = MaterialTheme.shapes.medium,
        enabled = enabled
    ) {
        Text(text = label, color = SuperIDTextWhite, fontSize = 18.sp)
    }
}