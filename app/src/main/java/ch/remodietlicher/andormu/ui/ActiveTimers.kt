package ch.remodietlicher.andormu.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun TimelineGraphicPreview() {
    ActiveTimers()
}

fun DrawScope.drawTag(start: Offset, end: Offset, color: Color) {
    val width = 50.dp.toPx()
    drawLine(
        color = color,
        start = start.copy(y = start.y + width / 2),
        end = end.copy(y = end.y + width / 2),
        strokeWidth = width
    )
}

// draw time axis for 24 hours
// fun DrawScope.timeAxis() {
//    // x axis
//    val xtickSpacing = drawLine(start = Offset())
// }

@Composable
fun ActiveTimers() {
    val scrollStateX = rememberScrollState()
    val scrollStateY = rememberScrollState()
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(100.dp)
                .clip(RectangleShape)
                .verticalScroll(scrollStateX)
                .horizontalScroll(scrollStateY)
    ) {
        Box(
            modifier =
                Modifier.size(1000.dp).drawBehind {
                    for (i in 0..100) {
                        drawLine(
                            color = Color.White,
                            start = Offset(i * 10.dp.toPx(), i * 10.dp.toPx()),
                            end = Offset(size.width, i * 10.dp.toPx()),
                            strokeWidth = 1f
                        )
                    }
                    drawLine(
                        color = Color.White,
                        start = Offset(100.dp.toPx(), 0.dp.toPx()),
                        end = Offset(100.dp.toPx(), size.height),
                        strokeWidth = 50f
                    )
                    drawTag(
                        start = Offset(0.dp.toPx(), 0.dp.toPx()),
                        end = Offset(100.dp.toPx(), 0.dp.toPx()),
                        color = Color.Red
                    )
                    //            timeAxis()
                }
        ) {
            Button(onClick = { /*TODO*/}) { Text(text = "Tag1") }
        }
    }
}
