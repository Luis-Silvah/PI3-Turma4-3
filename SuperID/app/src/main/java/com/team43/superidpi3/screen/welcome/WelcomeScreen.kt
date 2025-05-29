package com.team43.superidpi3.screen.welcome

import com.team43.superidpi3.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.team43.superidpi3.components.BtnPrimary
import com.team43.superidpi3.navigation.Routes

import com.team43.superidpi3.ui.theme.SuperIDButtonBlue
import com.team43.superidpi3.ui.theme.SuperIDTextWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen(navController: NavController) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val pages = listOf(
        R.drawable.welcome_01,
        R.drawable.welcome_01,
        R.drawable.welcome_01
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) { page ->
                Image(
                    painter = painterResource(id = pages[page]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Row(horizontalArrangement = Arrangement.Center) {
                repeat(pages.size) { index ->
                    val color = if (pagerState.currentPage == index)
                        SuperIDButtonBlue
                    else
                        Color.Gray.copy(alpha = 0.3f)

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    if (index < pages.size - 1) {
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = SuperIDTextWhite,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Bem Vindo ao Super")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = SuperIDButtonBlue,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Id")
                        }
                    },
                    modifier = Modifier.padding(top = 24.dp)
                )

                Text(
                    text = "Sua central segura para armazenar, organizar e acessar todas as suas senhas com praticidade e proteção.",
                    color = SuperIDTextWhite.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )

                BtnPrimary(
                    label = if (pagerState.currentPage == pages.size - 1) "Entrar" else "Próximo",
                    height = 54.dp,
                    enabled = true,
                    onClick = {
                        if (pagerState.currentPage < pages.size - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            navController.navigate(Routes.SignIn)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}


