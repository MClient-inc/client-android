package ru.mclient.ui.utils

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale

private val SlowOutSlowInEasing = CubicBezierEasing(0.3f, 0.3f, 0.3f, 1f)

fun Modifier.pulse(to: Float = 1.2f): Modifier {
    return composed {
        val infiniteTransition = rememberInfiniteTransition()
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = to,
            animationSpec = infiniteRepeatable(
                animation = tween(750, easing = SlowOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        Modifier.scale(scale)
    }
}