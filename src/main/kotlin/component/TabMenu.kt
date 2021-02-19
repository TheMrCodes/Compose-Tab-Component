package component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import helper.fitMaxWidth
import helper.hoverIndicator
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun TabMenu(
    onSelectionChange: (Int) -> Unit,
    tabLabels: List<String>,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
) {
    val primaryColor = MaterialTheme.colors.primary
    var selectedIndex by remember { mutableStateOf(0) }
    LaunchedEffect(selectedIndex) {
        onSelectionChange(selectedIndex)
    }

    Row(
        Modifier.fitMaxWidth().padding(horizontal = 4.dp).horizontalScroll(rememberScrollState()),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            val widthList: SnapshotStateList<IntSize> = remember { mutableStateListOf(*(Array(tabLabels.size) { IntSize(0, 0) })) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                tabLabels.forEachIndexed { i, it ->
                    Tab(widthList, it, i) { selectedIndex = i }
                }
            }

            // Animation
            val xAnimator = remember { Animatable(0f) }
            val widthAnimator = remember { Animatable(0f) }
            LaunchedEffect(selectedIndex) {
                coroutineScope {
                    launch { xAnimator.animateTo(widthList.take(selectedIndex).sumBy { it.width }.toFloat()) }
                    launch { widthAnimator.animateTo(widthList[selectedIndex].width.toFloat()) }
                }
            }

            // Canvas drawing
            Canvas(Modifier.height(52.dp)) {
                val preSize = widthAnimator.value
                val x = xAnimator.value
                drawPath(Path().apply {
                    moveTo(x, size.height - 2.dp.roundToPx())
                    lineTo(x, size.height)
                    lineTo(x + preSize, size.height)
                    lineTo(x + preSize, size.height - 2.dp.roundToPx())
                    close()
                }, color = primaryColor)
            }
        }
    }
}



@Composable
fun Tab(
    widthList: SnapshotStateList<IntSize>,
    text: String,
    index: Int,
    onSelect: () -> Unit,
) {
    Box(Modifier
        .onSizeChanged { widthList[index] = it }
        .clickable { onSelect() }
        .hoverIndicator()
        .height(52.dp)
        .padding(16.dp)
    ) { Text(text, color = MaterialTheme.colors.onBackground) }
}