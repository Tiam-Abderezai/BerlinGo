package com.example.berlingo.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.Dimensions.smallXXX

@Composable
fun Divider() = Divider(
    modifier = Modifier.padding(vertical = smallXXX),
    color = Color.Black,
    thickness = 0.8.dp,
)