package com.mambo.iris.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.mambo.iris.data.repository.MuseumRepository
import com.mambo.iris.ui.navigation.AppNavGraph
import com.mambo.iris.ui.theme.IrisAppTheme

class MainActivity : ComponentActivity() {

    private val museumRepository by lazy { MuseumRepository() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IrisAppTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    nav = navController,
                    repo = museumRepository
                )
            }
        }
    }
}






