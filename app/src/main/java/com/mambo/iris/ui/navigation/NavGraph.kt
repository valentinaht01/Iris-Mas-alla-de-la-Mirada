package com.mambo.iris.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mambo.iris.auth.AuthViewModel
import com.mambo.iris.data.repository.MuseumRepository
import com.mambo.iris.ui.screens.*
import com.mambo.iris.ui.screens.auth.LoginScreen
import com.mambo.iris.ui.screens.auth.SignUpScreen
import com.mambo.iris.ui.viewmodels.ChatViewModel
import com.mambo.iris.ui.viewmodels.ChatViewModelFactory

@Composable
fun AppNavGraph(
    nav: NavHostController,
    repo: MuseumRepository
) {
    Scaffold(
        bottomBar = {
            val navBackStackEntry by nav.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("/")
            if (currentRoute in listOf(
                    AppRoute.Home.route,
                    AppRoute.Bitacora.route,
                    AppRoute.Search.route,
                    AppRoute.Profile.route
                )
            ) {
                BottomNavigationBar(navController = nav)
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = AppRoute.Splash.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(AppRoute.Splash.route) {
                SplashScreen(navController = nav, repo = repo)
            }
            composable(AppRoute.Onboarding.route) {
                OnboardingScreen(navController = nav)
            }
            composable(AppRoute.Login.route) {
                val authVm: AuthViewModel = viewModel()
                LoginScreen(navController = nav, authViewModel = authVm)
            }
            composable(AppRoute.SignUp.route) {
                val authVm: AuthViewModel = viewModel()
                SignUpScreen(navController = nav, authViewModel = authVm)
            }
            composable(AppRoute.Home.route) {
                HomeScreen(navController = nav)
            }
            composable(AppRoute.Exhibitions.route) {
                ExhibitionsScreen(navController = nav)
            }
            composable(AppRoute.Gallery.route) {
                GalleryScreen(navController = nav, repo = repo)
            }
            composable(AppRoute.Chat.route) {
                val chatVm: ChatViewModel = viewModel(factory = ChatViewModelFactory(repo))
                ChatScreen(chatViewModel = chatVm)
            }
            composable(AppRoute.Bitacora.route) {
                BitacoraScreen()
            }
            composable(AppRoute.Search.route) {
                SearchScreen(navController = nav)
            }
            composable(AppRoute.Profile.route) {
                ProfileScreen()
            }
            composable(AppRoute.ArInstructions.route) {
                ArInstructionsScreen(navController = nav)
            }


        }
    }
}
