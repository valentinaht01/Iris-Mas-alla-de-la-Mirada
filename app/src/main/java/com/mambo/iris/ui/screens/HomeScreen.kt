package com.mambo.iris.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mambo.iris.R
import com.mambo.iris.ui.navigation.AppRoute
import com.mambo.iris.ui.theme.IrisAppTheme
import com.mambo.iris.ui.theme.workSansRegular

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_mambo),
            contentDescription = "Mambo Logo",
            modifier = Modifier.height(80.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.estructura_mambo_home_page),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(role = Role.Button) {
                        navController.navigate(AppRoute.Exhibitions.route) {
                            launchSingleTop = true
                        }
                    }
                    .padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explorar\nel museo",
                    color = Color(0xFFFDB813),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    lineHeight = 16.sp,
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF00AEEF),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "El edificio actual del MAMBO, ubicado en el centro cultural e histórico de la ciudad de Bogotá.",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontFamily = workSansRegular,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mambogota.com/el-museo/"))
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF58220)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Historia",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = workSansRegular
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.logo_fondo),
            contentDescription = null,
            modifier = Modifier
                .height(300.dp)
                .align(Alignment.End)
        )

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    IrisAppTheme {
        HomeScreen(navController = rememberNavController())
    }
}
