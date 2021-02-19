package helper

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min


fun Color.darker(scaler: Float = 0.1f): Color {
    return this.copy(
        red = max(this.red - 1f * scaler, 0f),
        green = max(this.green - 1f * scaler, 0f),
        blue = max(this.blue - 1f * scaler, 0f),
    )
}

fun Color.lighter(scaler: Float = 0.1f): Color {
    return this.copy(
        red = min(1f, this.red + 1f * scaler),
        green = min(1f, this.green + 1f * scaler),
        blue = min(1f, this.blue + 1f * scaler),
    )
}