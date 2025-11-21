package com.mambo.iris.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable fun Hotspot(mod:Modifier, onClick:()->Unit){
  Box(mod.size(28.dp).clip(CircleShape).background(Color(0xFF00BCD4)).border(2.dp, Color.White, CircleShape).clickable{ onClick() })
}