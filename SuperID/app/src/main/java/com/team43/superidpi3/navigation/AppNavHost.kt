package com.team43.superidpi3.navigation

import android.Manifest
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team43.superidpi3.components.Layout
import com.team43.superidpi3.screen.categoria.AddCategoriaScreen
import com.team43.superidpi3.screen.categoria.CategoriaScreen
import com.team43.superidpi3.screen.categoria.DeleteCategoriaScreen
import com.team43.superidpi3.screen.home.HomeScreen
import com.team43.superidpi3.screen.popup.PopupScreen
import com.team43.superidpi3.screen.qrcode.CameraAppScreen
import com.team43.superidpi3.screen.profile.ProfileScreen
import com.team43.superidpi3.screen.qrcode.WithPermission
import com.team43.superidpi3.screen.senha.AddSenhaScreen
import com.team43.superidpi3.screen.senha.DeleteSenhaScreen
import com.team43.superidpi3.screen.senha.EditSenhaScreen
import com.team43.superidpi3.screen.signin.SignInScreen
import com.team43.superidpi3.screen.signin.navigation.ForgotPasswordScreen
import com.team43.superidpi3.screen.signup.SignUpScreen
import com.team43.superidpi3.screen.splash.SplashScreen
import com.team43.superidpi3.screen.welcome.WelcomeScreen
import com.team43.superidpi3.ui.theme.SuperIDGrayPrimary
import com.team43.superidpi3.ui.theme.SuperIDWhite


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            SplashScreen(navController)
        }

        composable(Routes.Welcome) {
            WelcomeScreen(navController)
        }

        composable(
            route = Routes.Home,
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) {
            val idUsuario = it.arguments?.getString("idUsuario") ?: ""

            Layout(
                navController = navController,
                title = "Gerenciar Senhas",
                idUsuario = idUsuario,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate(Routes.addSenha(idUsuario)) },
                        containerColor = SuperIDGrayPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Adicionar Senha",
                            tint = SuperIDWhite
                        )
                    }
                }
            ) { padding ->
                HomeScreen(idUsuario, navController, padding)
            }
        }

        composable(
            route = Routes.Categoria,
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) {
            val idUsuario = it.arguments?.getString("idUsuario") ?: ""
            Layout(
                navController = navController,
                title = "Categorias",
                idUsuario = idUsuario,
                isSearch = false,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Routes.addCategoria(idUsuario))
                        },
                        containerColor = SuperIDGrayPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Adicionar Categoria",
                            tint = SuperIDWhite
                        )
                    }
                }
            ) { padding ->
                CategoriaScreen(navController, idUsuario, padding)
            }
        }

        composable(
            route = Routes.AddSenha,
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) {
            val idUsuario = it.arguments?.getString("idUsuario") ?: ""
            AddSenhaScreen(navController, idUsuario)
        }

        composable("delete_senha/{senhaId}/{categoriaNome}") { backStackEntry ->
            val senhaId = backStackEntry.arguments?.getString("senhaId") ?: ""
            val categoriaNome = backStackEntry.arguments?.getString("categoriaNome") ?: ""
            DeleteSenhaScreen(
                navController = navController,
                padding = PaddingValues(0.dp),
                senhaId = senhaId,
                categoriaNome = categoriaNome
            )
        }
        composable(
            route = "edit_senha/{senhaId}/{categoriaNome}",
            arguments = listOf(
                navArgument("senhaId") { type = NavType.StringType },
                navArgument("categoriaNome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val senhaId = backStackEntry.arguments?.getString("senhaId") ?: ""
            val categoriaNome = backStackEntry.arguments?.getString("categoriaNome") ?: ""

            EditSenhaScreen(
                navController = navController,
                padding = PaddingValues(0.dp),
                senhaId = senhaId,
                categoriaNome = categoriaNome
            )
        }


        composable("deletecategoria/{categoriaNome}/{idUsuario}") { backStackEntry ->
            val categoriaNome = backStackEntry.arguments?.getString("categoriaNome") ?: ""
            val idUsuario = backStackEntry.arguments?.getString("idUsuario") ?: ""
            DeleteCategoriaScreen(
                navController,
                padding = PaddingValues(0.dp),
                categoriaNome = categoriaNome,
                idUsuario = idUsuario
            )
        }

        composable(Routes.AddCategoria) { backStackEntry ->
            val idUsuario = backStackEntry.arguments?.getString("idUsuario") ?: ""
            AddCategoriaScreen(navController, idUsuario)
        }

        composable(Routes.SignUp) {
            SignUpScreen(navController)
        }

        composable(Routes.SignIn) {
            SignInScreen(navController)
        }

        composable(
            route = Routes.Qrcode,
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) {
            val idUsuario = it.arguments?.getString("idUsuario") ?: ""

            Layout(
                navController = navController,
                title = "QRcode",
                idUsuario = idUsuario,
                isSearch = false,
                floatingActionButton = {}
            ) { padding ->
                WithPermission(
                    modifier = Modifier.padding(padding),
                    permission = Manifest.permission.CAMERA
                ) {
                    CameraAppScreen(navController)
                }
            }
        }

        composable(Routes.ForgotPassword) {
            ForgotPasswordScreen(navController)
        }

        composable(
            route = Routes.Profile,
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) {
            val idUsuario = it.arguments?.getString("idUsuario") ?: ""

            Layout(
                navController, "Perfil", idUsuario, isSearch = false, isProfile = false,
                floatingActionButton = {}) { padding ->
                ProfileScreen(idUsuario, navController, padding)
            }
        }

        composable(
            route = Routes.Popup,
            arguments = listOf(
                navArgument("idUsuario") { type = NavType.StringType },
                navArgument("status") { type = NavType.StringType },
                navArgument("mensagem") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) {
            val idUsuario = it.arguments?.getString("idUsuario") ?: ""
            val status = it.arguments?.getString("status") ?: "error"
            val mensagem = it.arguments?.getString("mensagem") ?: ""

            PopupScreen(
                idUsuario = idUsuario,
                status = status,
                mensagem = mensagem,
                navController = navController
            )
        }

    }
}

