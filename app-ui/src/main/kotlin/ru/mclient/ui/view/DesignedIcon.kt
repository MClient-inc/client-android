package ru.mclient.ui.view

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

sealed interface DesignedDrawable {

    data class Resource(val icon: Int, val contentDescription: DesignedString? = null) :
        DesignedDrawable

    data class Painter(
        val icon: androidx.compose.ui.graphics.painter.Painter,
        val contentDescription: DesignedString? = null
    ) : DesignedDrawable

    data class ImageBitmap(
        val icon: androidx.compose.ui.graphics.ImageBitmap,
        val contentDescription: DesignedString? = null
    ) : DesignedDrawable

    data class ImageVector(
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val contentDescription: DesignedString? = null
    ) : DesignedDrawable

}


fun Painter.toDesignedDrawable(contentDescription: DesignedString? = null): DesignedDrawable.Painter {
    return DesignedDrawable.Painter(this, contentDescription)
}

fun Int.toDesignedDrawable(contentDescription: DesignedString? = null): DesignedDrawable.Resource {
    return DesignedDrawable.Resource(this, contentDescription)
}

fun ImageBitmap.toDesignedDrawable(contentDescription: DesignedString? = null): DesignedDrawable.ImageBitmap {
    return DesignedDrawable.ImageBitmap(this, contentDescription)
}

fun ImageVector.toDesignedDrawable(contentDescription: DesignedString? = null): DesignedDrawable.ImageVector {
    return DesignedDrawable.ImageVector(this, contentDescription)
}

@Composable
fun DesignedIcon(
    icon: DesignedDrawable,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    when (icon) {
        is DesignedDrawable.Painter ->
            Icon(
                painter = icon.icon,
                contentDescription = icon.contentDescription?.toStringComposable(),
                modifier = modifier,
                tint = tint,
            )

        is DesignedDrawable.Resource ->
            Icon(
                painter = painterResource(icon.icon),
                contentDescription = icon.contentDescription?.toStringComposable(),
                modifier = modifier,
                tint = tint,
            )

        is DesignedDrawable.ImageBitmap ->
            Icon(
                bitmap = icon.icon,
                contentDescription = icon.contentDescription?.toStringComposable(),
                modifier = modifier,
                tint = tint,
            )

        is DesignedDrawable.ImageVector ->
            Icon(
                imageVector = icon.icon,
                contentDescription = icon.contentDescription?.toStringComposable(),
                modifier = modifier,
                tint = tint,
            )

    }
}