package com.team43.superidpi3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team43.superidpi3.navigation.Routes
import com.team43.superidpi3.ui.theme.SuperIDBackground
import com.team43.superidpi3.ui.theme.SuperIDGrayPrimary
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import com.team43.superidpi3.ui.theme.SuperIDWhite

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: () -> Unit
)

@Composable
fun Layout(
    routeIndex: Int = 0,
    navController: NavController,
    title: String,
    idUsuario: String,
    isSearch: Boolean = true,
    isProfile: Boolean = true,
    showFab: Boolean = true,
    onFabClick: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val navBar = listOf(
        NavItem("Home", Icons.Default.Home, { navController.navigate(Routes.home(idUsuario)) }),
        NavItem("QR Code", Icons.Default.QrCode, { navController.navigate(Routes.Qrcode) }),
        NavItem("Categorias", Icons.Default.Category, { navController.navigate(Routes.categoria(idUsuario)) })
    )

    var selectedItemIndex by remember { mutableIntStateOf(routeIndex) }

    Scaffold(
        containerColor = SuperIDBackground,
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = {navController.navigate(Routes.addSenha(idUsuario))},
                    containerColor = SuperIDGrayPrimary
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Adicionar",
                        tint = SuperIDWhite
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        top = 32.dp, bottom = 8.dp,
                        start = 24.dp,
                        end = 24.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = SuperIDTextWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    if(isSearch) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    color = SuperIDTextWhite.copy(alpha = 0.05f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = SuperIDTextWhite,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    if(isProfile) {
                        IconButton(
                            onClick = { navController.navigate(Routes.profile(idUsuario)) },
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    color = SuperIDTextWhite.copy(alpha = 0.05f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Perfil",
                                tint = SuperIDTextWhite,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier,
                containerColor = SuperIDGrayPrimary.copy(alpha = 0.5f),
                contentColor = SuperIDWhite
            ) {
                navBar.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(text = item.label) },
                        selected = selectedItemIndex == index,
                        onClick = { item.route() },
                        colors = NavigationBarItemColors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = SuperIDWhite,
                            selectedIndicatorColor = SuperIDWhite,
                            unselectedIconColor = SuperIDWhite,
                            unselectedTextColor = SuperIDWhite,
                            disabledIconColor = SuperIDWhite,
                            disabledTextColor = SuperIDWhite,
                        )
                    )
                }
            }
        },
        content = { padding -> content(padding) },
    )
}