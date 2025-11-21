package com.mambo.iris.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mambo.iris.R
import com.mambo.iris.ui.theme.workSansRegular

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Bitacora,
        BottomNavItem.Search,
        BottomNavItem.Profile
    )
    NavigationBar(
        containerColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title, fontFamily = workSansRegular) },
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFF58220),
                    unselectedIconColor = Color.Black,
                    selectedTextColor = Color(0xFFF58220),
                    unselectedTextColor = Color.Black,
                    indicatorColor = Color.White
                )
            )
        }
    }
}

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){
    object Home : BottomNavItem("Home", R.drawable.ic_home, AppRoute.Home.route)
    object Bitacora : BottomNavItem("Bitácora", R.drawable.ic_bitacora, AppRoute.Bitacora.route)
    object Search : BottomNavItem("Buscar", R.drawable.ic_search, AppRoute.Search.route)
    object Profile : BottomNavItem("Perfil", R.drawable.ic_profile, AppRoute.Profile.route)
}