package com.example.berlingo.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.Divider

@Composable
fun Divider() = Divider(
    modifier = Modifier.padding(vertical = 8.dp),
    color = Color.Black,
    thickness = 0.8.dp,
)