package com.mambo.iris.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mambo.iris.R
import com.mambo.iris.ui.navigation.AppRoute
import com.mambo.iris.ui.theme.IrisAppTheme
import com.mambo.iris.ui.theme.highTowerFontFamily
import com.mambo.iris.ui.theme.workSansRegular

@Composable
fun ExhibitionsScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
    ) {
        Image(
            painter = painterResource(id = R.drawable.estructura_mambo_home_page),
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomEnd).height(480.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.2f
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.End).padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFFFDB813))
            }
            Image(
                painter = painterResource(id = R.drawable.logo_mambo),
                contentDescription = "Mambo Logo",
                modifier = Modifier.height(55.dp)
            )
            
            Spacer(modifier = Modifier.weight(0.3f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 40.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "PISO", color = Color.White, fontFamily = workSansRegular)
                    Text(text = "CICLO EXPOSITIVO 2025", color = Color.White, fontFamily = workSansRegular)
                }
                Spacer(modifier = Modifier.height(24.dp))
                // AR functionality is disabled
                ExhibitionItem(navController = navController, floor = "3", title = "Inflaleve. Memoria y fragilidad en la obra de Oscar Muñoz", modelName = "")
                ExhibitionItem(navController = navController, floor = "2", title = "Secuencias. La imagen - montage Sair García", modelName = "")
                ExhibitionItem(navController = navController, floor = "I", title = "Colección de obras Museo MAMBO", modelName = "", route = AppRoute.Search.route, isRoman = true)
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ExhibitionItem(
    navController: NavController, 
    floor: String, 
    title: String, 
    modelName: String, 
    route: String? = null, 
    isRoman: Boolean = false
) {
    Column(modifier = Modifier.clickable {
        if (route != null) {
            navController.navigate(route)
        } else if (modelName.isNotBlank()) {
            // AR Navigation is currently disabled project-wide
        }
    }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = floor,
                color = Color(0xFFFDB813),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 16.dp),
                fontFamily = if(isRoman) highTowerFontFamily else null
            )
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = workSansRegular
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF00AEEF))
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ExhibitionsScreenPreview() {
    IrisAppTheme {
        ExhibitionsScreen(rememberNavController())
    }
}
