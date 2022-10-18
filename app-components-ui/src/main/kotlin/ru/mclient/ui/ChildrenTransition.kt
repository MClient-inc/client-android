package ru.mclient.ui

import androidx.compose.animation.core.spring
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.scale


val LocalChildrenStackAnimator = staticCompositionLocalOf {
    fade(spring()) + scale(spring(), frontFactor = 0.8f, backFactor = 1.2f)
}