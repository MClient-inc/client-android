package ru.mclient.ui.utils

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun currentTime(threshold: Long = 1000): Long {
    var ticks by remember { mutableStateOf(System.currentTimeMillis()) }
    val repeatable = rememberInfiniteTransition()
    LaunchedEffect(threshold) {
        while (true) {
            delay(threshold)
            ticks = System.currentTimeMillis()
        }
    }
    return ticks
}