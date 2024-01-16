package com.example.berlingo.common.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import kotlinx.coroutines.delay
import kotlin.random.Random

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("LoadingScreen()")

@Composable
fun LoadingScreen() {
    logger.debug("LOADING")
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        RandomDotsAnimation()
    }
}

@Composable
fun RandomDotsAnimation() {
    Column() {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val dots = remember { mutableStateListOf<Boolean>().apply { repeat(1) { add(true) } } }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    delay(50) // Time between changes
                    dots[Random.nextInt(dots.size)] = dots.random().not()
                }
            }

            dots.forEach { isVisible ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(10.dp),
                        strokeWidth = 8.dp,
                        color = Color.Yellow,
                    )
                }
            }
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val dots = remember { mutableStateListOf<Boolean>().apply { repeat(2) { add(true) } } }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    delay(50) // Time between changes
                    dots[Random.nextInt(dots.size)] = dots.random().not()
                }
            }

            dots.forEach { isVisible ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(10.dp),
                        strokeWidth = 8.dp,
                        color = Color.Yellow,
                    )
                }
            }
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val dots = remember { mutableStateListOf<Boolean>().apply { repeat(4) { add(true) } } }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    delay(50) // Time between changes
                    dots[Random.nextInt(dots.size)] = dots.random().not()
                }
            }

            dots.forEach { isVisible ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(10.dp),
                        strokeWidth = 8.dp,
                        color = Color.Yellow,
                    )
                }
            }
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val dots = remember { mutableStateListOf<Boolean>().apply { repeat(8) { add(true) } } }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    delay(50) // Time between changes
                    dots[Random.nextInt(dots.size)] = dots.random().not()
                }
            }

            dots.forEach { isVisible ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(10.dp),
                        strokeWidth = 8.dp,
                        color = Color.Yellow,
                    )
                }
            }
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val dots = remember { mutableStateListOf<Boolean>().apply { repeat(4) { add(true) } } }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    delay(50) // Time between changes
                    dots[Random.nextInt(dots.size)] = dots.random().not()
                }
            }

            dots.forEach { isVisible ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(10.dp),
                        strokeWidth = 8.dp,
                        color = Color.Yellow,
                    )
                }
            }
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val dots = remember { mutableStateListOf<Boolean>().apply { repeat(2) { add(true) } } }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    delay(50) // Time between changes
                    dots[Random.nextInt(dots.size)] = dots.random().not()
                }
            }

            dots.forEach { isVisible ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(10.dp),
                        strokeWidth = 8.dp,
                        color = Color.Yellow,
                    )
                }
            }
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val dots = remember { mutableStateListOf<Boolean>().apply { repeat(1) { add(true) } } }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    delay(50) // Time between changes
                    dots[Random.nextInt(dots.size)] = dots.random().not()
                }
            }

            dots.forEach { isVisible ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(10.dp),
                        strokeWidth = 8.dp,
                        color = Color.Yellow,
                    )
                }
            }
        }
    }
}
