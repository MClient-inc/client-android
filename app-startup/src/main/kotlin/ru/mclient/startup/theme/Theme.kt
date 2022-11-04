package ru.mclient.startup.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.compose.material3.MaterialTheme as MaterialTheme3

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> Theme3.Light.material3Colors
        else -> Theme3.Light.material3Colors
    }
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colors = Theme3.Light.materialColors,
    ) {
        MaterialTheme3(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}


internal sealed class Theme3(val material3Colors: ColorScheme, val materialColors: Colors) {
    object Light : Theme3(
        ColorScheme(
            primary = lightPrimary,
            onPrimary = lightOnPrimary,
            primaryContainer = lightPrimaryContainer,
            onPrimaryContainer = lightOnPrimaryContainer,
            inversePrimary = lightUnknown,
            secondary = lightSecondary,
            onSecondary = lightOnSecondary,
            secondaryContainer = lightSecondaryContainer,
            onSecondaryContainer = lightOnSecondaryContainer,
            tertiary = lightUnknown,
            onTertiary = lightOnUnknown,
            tertiaryContainer = lightUnknown,
            onTertiaryContainer = lightOnUnknown,
            background = lightBackground,
            onBackground = lightOnBackground,
            surface = lightSurface,
            onSurface = lightOnSurface,
            surfaceVariant = lightSurfaceVariant,
            onSurfaceVariant = lightOnSurfaceVariant,
            surfaceTint = lightSurfaceTint,
            inverseSurface = lightUnknown,
            inverseOnSurface = lightOnUnknown,
            error = lightError,
            onError = lightOnError,
            errorContainer = lightUnknown,
            onErrorContainer = lightOnUnknown,
            outline = lightOutline,
            outlineVariant = lightOutlineVariant,
            scrim = scrim
        ),
        Colors(
            primary = lightPrimary,
            onPrimary = lightOnPrimary,
            secondary = lightSecondary,
            onSecondary = lightOnSecondary,
            background = lightBackground,
            onBackground = lightOnBackground,
            surface = lightSurface,
            onSurface = lightOnSurface,
            error = lightError,
            onError = lightOnError,
            isLight = true,
            primaryVariant = lightPrimaryVariant,
            secondaryVariant = lightSecondaryVariant
        )
    )
}