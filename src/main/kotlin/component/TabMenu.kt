package component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import helper.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt


@ExperimentalMaterialApi
@Composable
fun TabMenu(
    tabLabels: List<String>,
    selectedIndex: MutableState<Int> = rememberMutableStateOf(0),
    modifier: Modifier = Modifier,
    fillFullSpace: Boolean = false, // test implementation for stretched width tabs
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    onSelectionChange: (Int) -> Unit = { selectedIndex.value = it },
    content: @Composable (Int) -> Unit = {},
) {
    val primaryColor = MaterialTheme.colors.primary

    BoxWithConstraints(
        modifier = Modifier.fitMaxWidth().then(modifier)
    ) {
        val constraints = this
        val width = constraints.maxWidth.toPx()
        val swipeState = rememberSwipeableState(0, confirmStateChange = { onSelectionChange(it); true })
        val anchors = remember(tabLabels, width) { tabLabels.mapIndexed { i, _ -> -(width * i) to i }.toMap() }

        LaunchedEffect(selectedIndex.value) {
            swipeState.animateTo(selectedIndex.value)
        }

        Column(modifier = Modifier.fitMaxWidth()) {
            // Tab menu
            val progressBarDelta = (-swipeState.offset.value / width)
            Row(
                Modifier.fitMaxWidth().padding(horizontal = 4.dp).horizontalScroll(rememberScrollState()),
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    val widthList: SnapshotStateList<Float> = remember { mutableStateListOf(*(Array(tabLabels.size) { 0f })) }
                    val canExpand = fillFullSpace && widthList.sum() <= width
                    Row(Modifier.thenIf(canExpand) { width(constraints.maxWidth) }, verticalAlignment = Alignment.CenterVertically) {
                        tabLabels.forEachIndexed { i, label ->
                            Box(Modifier
                                .clickable { onSelectionChange(i) }
                                .onSizeChanged { widthList[i] = it.width.toFloat() }
                                .thenIf(canExpand) { weight(1f) }
                                .hoverIndicator()
                                .height(52.dp)
                                .padding(16.dp),
                                Alignment.Center
                            ) {
                                Text(label, color = MaterialTheme.colors.onBackground)
                            }
                        }
                    }

                    // Animation
                    val xAnimator = remember { Animatable(0f) }
                    val widthAnimator = remember(widthList.first()) { Animatable(widthList.first()) }
                    LaunchedEffect(selectedIndex.value) {
                        coroutineScope {
                            launch { xAnimator.animateTo(widthList.take(selectedIndex.value).sum()) }
                            launch { widthAnimator.animateTo(widthList[selectedIndex.value]) }
                        }
                    }
                    LaunchedEffect(progressBarDelta) {
                        var left = progressBarDelta
                        val endX = widthList.take(ceil(progressBarDelta).toInt()).sumByDouble {
                            val ret = (it * min(1f, left)).toDouble()
                            left -= min(1f, left)
                            ret
                        }.toFloat()
                        xAnimator.snapTo(endX)
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


            //Content
            //println(progressBarDelta)
            Box(
                modifier = Modifier
                    .fitMaxWidth()
                    .swipeable(
                        state = swipeState,
                        anchors = anchors,
                        orientation = Orientation.Horizontal,
                        thresholds =  { _, _ -> FractionalThreshold(0.5f) },
                        resistance = null
                    )
            ) {
                if (selectedIndex.value > 0) {
                    Box(Modifier.fitMaxWidth().offset { IntOffset((swipeState.offset.value + (selectedIndex.value - 1)*width).roundToInt(), 0) }) {
                        content(selectedIndex.value - 1)
                    }
                }
                Box(Modifier.fitMaxWidth().offset { IntOffset((swipeState.offset.value + selectedIndex.value*width).roundToInt(), 0) }) {
                    content(selectedIndex.value)
                }
                if (selectedIndex.value < tabLabels.size - 1) {
                    Box(Modifier.fitMaxWidth().offset { IntOffset((swipeState.offset.value + (selectedIndex.value + 1)*width).roundToInt(), 0) }) {
                        content(selectedIndex.value + 1)
                    }
                }
            }
        }
    }
}