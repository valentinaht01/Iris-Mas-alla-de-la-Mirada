package com.mambo.iris.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mambo.iris.auth.AuthViewModel
import com.mambo.iris.ui.screens.SplashScreen
import com.mambo.iris.ui.screens.OnboardingScreen
import com.mambo.iris.ui.screens.HomeScreen
import com.mambo.iris.ui.screens.GalleryScreen
import com.mambo.iris.ui.screens.ChatScreen
import com.mambo.iris.ui.screens.chat.ChatViewModel
import com.mambo.iris.ui.screens.LoginScreen
import com.mambo.iris.ui.screens.SignUpScreen
@Composable
fun AppNavGraph(
    nav: NavHostController

) {
    NavHost(
        navController = nav,
        startDestination = AppRoute.Splash.route
    ) {
        // Splash
        composable(AppRoute.Splash.route) {
            SplashScreen(nav)
        }

        // Onboarding
        composable(AppRoute.Onboarding.route) {
            OnboardingScreen(navController = nav)
        }

        // Login
        composable(AppRoute.Login.route) {
            LoginScreen(nav)
        }

        // Sign Up
        composable(AppRoute.SignUp.route) {
            val authViewModel: AuthViewModel = viewModel()
            SignUpScreen(
                authViewModel = authViewModel,
                onRegistrationSuccess = {
                    nav.navigate(AppRoute.Home.route) {
                        // Limpia el flujo de onboarding para que no regrese atrás
                        popUpTo(AppRoute.Onboarding.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Home
        composable(AppRoute.Home.route) {
            HomeScreen(navController = nav)
        }

        // Gallery
        composable(AppRoute.Gallery.route) {
            // GalleryScreen ya no recibe parámetros extra,
            // obtiene su ViewModel a través de su propio Factory.
            GalleryScreen(navController = nav)
        }

        // Chat
        composable(AppRoute.Chat.route) {
            val chatViewModel: ChatViewModel = viewModel()
            ChatScreen(chatViewModel = chatViewModel)
        }
    }
}

