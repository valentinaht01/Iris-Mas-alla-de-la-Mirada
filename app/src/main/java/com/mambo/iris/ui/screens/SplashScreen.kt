package com.mambo.iris.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mambo.iris.data.repository.MuseumRepository
import com.mambo.iris.ui.navigation.AppRoute
import com.mambo.iris.ui.theme.highTowerFontFamily
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, repo: MuseumRepository) {
    var circleState by remember { mutableStateOf(CircleAnimationState.START) }
    var textState by remember { mutableStateOf(false) }
    var animatedText by remember { mutableStateOf("") }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val circleOffsetY by animateDpAsState(
        targetValue = if (circleState >= CircleAnimationState.MIDDLE) 0.dp else -screenHeight / 2,
        animationSpec = tween(durationMillis = 800),
        label = "Circle Offset Y"
    )

    val circleScale by animateFloatAsState(
        targetValue = if (circleState == CircleAnimationState.EXPANDED) 20f else 1f,
        animationSpec = tween(durationMillis = 1000),
        label = "Circle Scale"
    )

    LaunchedEffect(Unit) {
        delay(200)
        circleState = CircleAnimationState.MIDDLE
        delay(800)
        circleState = CircleAnimationState.EXPANDED
        delay(800)
        textState = true
        delay(300); animatedText = "I"
        delay(150); animatedText = "IR"
        delay(150); animatedText = "IRI"
        delay(150); animatedText = "IRIS"
        delay(1500)

        val destination = if (repo.hasUser()) {
            AppRoute.Home.route
        } else {
            AppRoute.Onboarding.route
        }

        navController.navigate(destination) {
            popUpTo(AppRoute.Splash.route) { inclusive = true }
        }
    }

    val textColor: Color =
        if (circleState == CircleAnimationState.START || circleState == CircleAnimationState.MIDDLE)
            Color.Black
        else
            Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .offset(y = circleOffsetY)
                .scale(circleScale)
                .size(100.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        )

        if (textState) {
            Text(
                text = animatedText,
                color = textColor,
                fontSize = 90.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = highTowerFontFamily
            )
        }
    }
}

private enum class CircleAnimationState { START, MIDDLE, EXPANDED }



