package helper

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import theme.LocalThemeIsDark


fun Modifier.thenIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    if (!condition) return this
    return modifier.invoke(this)
}


@Composable
fun Modifier.hoverIndicator(): Modifier {
    val isDark = LocalThemeIsDark.current
    val back = MaterialTheme.colors.background
    var color by remember(back) { mutableStateOf<Color?>(null) }

    return this
        // "thenIf" same as ".run { if (color != null) background(color = color!!) else this }"
        .thenIf(color != null) { background(color = color!!) }
        .pointerMoveFilter(
            onEnter = { color = if (!isDark) back.darker() else back.lighter(); false },
            onExit = { color = null; false },
        )
}