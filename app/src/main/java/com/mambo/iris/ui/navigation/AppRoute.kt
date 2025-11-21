package com.mambo.iris.ui.navigation

import androidx.navigation.NamedNavArgument

sealed class AppRoute(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {
    object Splash : AppRoute("splash")
    object Onboarding : AppRoute("onboarding")
    object Login : AppRoute("login")
    object SignUp : AppRoute("signup")
    object Home : AppRoute("home")
    object Gallery : AppRoute("gallery")
    object Auth : AppRoute("auth")
    object Main : AppRoute("main")
    object Chat : AppRoute("chat")
    object Bitacora : AppRoute("bitacora")
    object Search : AppRoute("search")
    object Profile : AppRoute("profile")
    object Exhibitions : AppRoute("exhibitions")
    object ArInstructions : AppRoute("ar_instructions")
    object ArExperience : AppRoute("ar_experience")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route.split("/").first())
            args.forEach {
                append("/$it")
            }
        }
    }
}
