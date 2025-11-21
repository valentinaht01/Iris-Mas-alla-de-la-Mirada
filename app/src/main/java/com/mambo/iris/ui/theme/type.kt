package com.mambo.iris.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mambo.iris.R


val highTowerFontFamily = FontFamily(
    Font(R.font.high_tower, FontWeight.Normal)
)


val workSansBold = FontFamily(

    Font(R.font.work_sans_bold, FontWeight.Bold)
)


val workSansRegular = FontFamily(
    Font(R.font.work_sans_regular, FontWeight.Normal)
)


val AppTypography = Typography(

    displayLarge = TextStyle(
        fontFamily = highTowerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp
    ),
    displayMedium = TextStyle(
        fontFamily = highTowerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = workSansRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),


    labelLarge = TextStyle(
        fontFamily = workSansBold,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
)
