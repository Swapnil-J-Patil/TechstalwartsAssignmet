package com.swapnil.techstalwartsassignmet.ui.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.swapnil.techstalwartsassignmet.R
import com.swapnil.techstalwartsassignmet.ui.theme.blue
import com.swapnil.techstalwartsassignmet.ui.theme.lightBlue
import com.swapnil.techstalwartsassignmet.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavHostController, viewModel: LoginViewModel) {
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(0f) }
    val offsetY = remember { Animatable(1000f) } // Initial offset for text animation

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = { it } // Linear interpolation
            )
        )
        scope.launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = { it } // Linear interpolation
                )
            )
        }
        delay(2000L) // Wait for 2 seconds
        if (viewModel.token.value != null) {
            navController.navigate("user_screen") {
                popUpTo("splash_screen") { inclusive = true }
            }
        } else {
            navController.navigate("login_screen") {
                popUpTo("splash_screen") { inclusive = true }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(lightBlue)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.profilehub), // Replace with your image
                contentDescription = "Splash Image",
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale.value),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ProfileHub",
                color = Color.White,
                style= TextStyle(
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.offset(y = offsetY.value.dp)
            )
        }
    }
}