package ru.mclient.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.placeholder

fun Modifier.defaultPlaceholder(): Modifier {
    return composed {
        Modifier.placeholder(true, MaterialTheme.colorScheme.scrim, MaterialTheme.shapes.small)
    }
}