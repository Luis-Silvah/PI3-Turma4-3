package com.team43.superidpi3.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.ui.theme.SuperIDBackground
import com.team43.superidpi3.ui.theme.SuperIDGrayPrimary
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import com.team43.superidpi3.ui.theme.SuperIDWhite

data class NavItem(
    val label: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("AutoboxingStateCreation", "RememberReturnType")
@Composable
fun HomeScreen(idUsuario: String, navController: NavController) {
    val ctx = LocalContext.current
    val HomeActions = remember { HomeActions(ctx, navController) }
    val navBar = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("QR Code", Icons.Default.QrCode),
        NavItem("Categorias", Icons.Default.Category)
    )

    var selectedItemIndex by remember { mutableStateOf(0) }

//
//    val usuarioState = remember { mutableStateOf<Map<String, Any>?>(null) }
//
//    if (!idUsuario.isNullOrEmpty()) {
//        HomeActions.buscaUsuario(idUsuario) { dados ->
//            usuarioState.value = dados
//        }
//    } else {
//        Log.e("uid", "UID não encontrado")
//    }
//
//    Column {
//        usuarioState.value?.let { usuario ->
//            Text("Nome: ${usuario["nome"] ?: "Não informado"}")
//            Text("Email: ${usuario["email"] ?: "Não informado"}")
//            Text(text = "UID: $idUsuario")
//
//
//
////            if (usuario["emailVerificado"]) {
////                Button(
////                    onClick = {
////                        VerificarEmail(ctx).verifica { emailVerificado ->
////                            if (emailVerificado) {
////                                Toast.makeText(ctx, "Email verificado com sucesso!", Toast.LENGTH_SHORT).show()
////                            } else {
////                                Toast.makeText(ctx, "Seu email ainda não foi verificado.", Toast.LENGTH_SHORT).show()
////                            }
////                        }
////                    },
////                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E94)),
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(top = 24.dp)
////                        .height(50.dp)
////                ) {
////                    Text(text = "Já verifiquei o email")
////                }
////            }
//        }
//
//        BtnPrimary("Sair", 54.dp, true, {HomeActions.logout()})
//
//
//    }


    Scaffold(
        containerColor = SuperIDBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = SuperIDGrayPrimary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add, contentDescription = "Adicionar",
                    modifier = Modifier,
                    tint = SuperIDWhite
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding),
                horizontalAlignment = Alignment.Start
            ) {
                BtnPrimary("Sair", 54.dp, true, { HomeActions.logout() })
            }
        },
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
                    text = "Gerenciar Senhas",
                    color = SuperIDTextWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.weight(1f))
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
                Spacer(modifier = Modifier.weight(0.5f))
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
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Perfil",
                        tint = SuperIDTextWhite,
                        modifier = Modifier.size(28.dp)
                    )
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
                        onClick = { selectedItemIndex = index },
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
        }
    )


}
