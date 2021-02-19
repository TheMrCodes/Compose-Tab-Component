package theme

import androidx.compose.desktop.DesktopTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color


private val LightThemeColors = lightColors(
    primary = Color(0, 120, 216),
    primaryVariant = Color(0, 98, 179),
    secondary = Color.Black,
    secondaryVariant = Color.Black,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
)
private val DarkThemeColors = darkColors(
    primary = Color(0, 120, 216),
    primaryVariant = Color(0, 140, 255),
    secondary = Color.White,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

val LocalThemeIsDark = compositionLocalOf { false }


@Composable
fun AppTheme(
    isDarkTheme: Boolean = true,
    colors: Colors? = null,
    content: @Composable () -> Unit
) {
    val myColors = colors ?: if (isDarkTheme) DarkThemeColors else LightThemeColors

    MaterialTheme(colors = myColors) {
        DesktopTheme {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colors.onBackground,
                LocalThemeIsDark provides isDarkTheme,
                content = content
            )
        }
    }
}
