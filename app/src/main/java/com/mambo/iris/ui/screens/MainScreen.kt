package com.mambo.iris.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mambo.iris.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val iconResId: Int
) {
    object Home : BottomBarScreen("home", "Home", R.drawable.ic_home)
    object Bitacora : BottomBarScreen("bitacora", "Bitácora", R.drawable.ic_bitacora)
    object Buscar : BottomBarScreen("buscar", "Buscar", R.drawable.ic_search)
    object Perfil : BottomBarScreen("perfil", "Perfil", R.drawable.ic_profile)
}

private val bottomBarItems = listOf(
    BottomBarScreen.Home,
    BottomBarScreen.Bitacora,
    BottomBarScreen.Buscar,
    BottomBarScreen.Perfil
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(rootNav: NavController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController = bottomNavController) }
    ) { paddingValues ->
        BottomNavGraph(
            bottomNavController = bottomNavController,
            rootNav = rootNav, // <- pasamos el nav raíz hacia adentro
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun AppBottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomBarItems.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                label = {
                    Text(
                        text = screen.title,
                        fontSize = 10.sp
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.iconResId),
                        contentDescription = screen.title
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFFA500),
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color(0xFFFFA500),
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent // Sin píldora de fondo
                )
            )
        }
    }
}

@Composable
fun BottomNavGraph(
    bottomNavController: NavHostController,
    rootNav: NavController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = bottomNavController,
        startDestination = BottomBarScreen.Home.route,
        modifier = modifier
    ) {
        composable(route = BottomBarScreen.Home.route) {
            // Muy importante: pasar el rootNav al HomeScreen
            HomeScreen(navController = rootNav)
        }
        composable(route = BottomBarScreen.Bitacora.route) {
            Box(modifier = Modifier.padding())
            Text("Pantalla de Bitácora")
        }
        composable(route = BottomBarScreen.Buscar.route) {
            Text("Pantalla de Búsqueda")
        }
        composable(route = BottomBarScreen.Perfil.route) {
            Text("Pantalla de Perfil")
        }
    }
}

