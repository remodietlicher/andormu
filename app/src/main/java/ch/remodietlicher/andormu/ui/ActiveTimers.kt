package ch.remodietlicher.andormu.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import ch.remodietlicher.andormu.database.TaggedTimer
import ch.remodietlicher.andormu.model.TaggedTimerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.lang.Long.max
import java.lang.Long.min
import java.util.*

private const val TAG = "TimelineScreen"

@Preview
@Composable
fun TimelineGraphicPreview() {
    ActiveTimers(Scale.DAY)
}

enum class Scale {
    DAY,
    HOUR,
}

@Composable
fun ActiveTimers(scale: Scale) {
    val scrollStateX = rememberScrollState()
    val scrollStateY = rememberScrollState()
    var currentTime by remember { mutableStateOf(Date().time) }

    val viewModel = remember { TaggedTimerViewModel() }
    val timers: List<TaggedTimer> by viewModel.timers.collectAsState()

    LaunchedEffect(true) {
        flow {
                while (true) {
                    emit(Date().time)
                    delay(1000)
                }
            }
            .collect { currentTime = it }
    }

    val totalSize = 1000.dp

    val numberOfLines =
        when (scale) {
            Scale.DAY -> 24
            Scale.HOUR -> 60
        }

    val lineSpacing =
        when (scale) {
            Scale.DAY -> totalSize / 24
            Scale.HOUR -> totalSize / 60
        }

    val beginningOfScale =
        when (scale) {
            Scale.DAY -> viewModel.getBeginningOfDay()
            Scale.HOUR -> viewModel.getBeginningOfHour()
        }

    val endOfScale =
        when (scale) {
            Scale.DAY -> viewModel.getEndOfDay()
            Scale.HOUR -> viewModel.getEndOfHour()
        }

    val millisecondsOnScale =
        when (scale) {
            Scale.DAY -> 24 * 60 * 60 * 1000
            Scale.HOUR -> 60 * 60 * 1000
        }

    val pixelsPerMillisecond = totalSize / millisecondsOnScale

    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(500.dp)
                .clip(RectangleShape)
                .verticalScroll(scrollStateX)
                .horizontalScroll(scrollStateY)
    ) {
        Box(
            modifier =
                Modifier.size(totalSize).drawBehind {

                    // draw line for current time
                    val currentX =
                        (currentTime - beginningOfScale.time).toInt() * pixelsPerMillisecond
                    drawLine(
                        color = Color.Red,
                        start = Offset(currentX.toPx(), 0f),
                        end = Offset(currentX.toPx(), size.height),
                        strokeWidth = 1f
                    )
                    // draw time axis for 24 hours
                    for (i in 0..numberOfLines) {
                        drawLine(
                            color = Color.White,
                            start = Offset(i * lineSpacing.toPx(), 0f),
                            end = Offset(i * lineSpacing.toPx(), size.height),
                            strokeWidth = 1f
                        )
                    }
                }
        ) {
            timers.forEachIndexed { index, taggedTimer ->
                // all times in milliseconds
                val start = max(taggedTimer.startTime.time, beginningOfScale.time)
                val end = min(taggedTimer.endTime?.time ?: currentTime, endOfScale.time)
                val duration = end - start

                val x = (start - beginningOfScale.time).toInt() * pixelsPerMillisecond
                val width = duration.toInt() * pixelsPerMillisecond

                val height = 50.dp

                // draw a button for each timer
                Button(
                    onClick = { /*TODO*/},
                    modifier =
                        Modifier.absoluteOffset(x = x, y = index * height).size(width, height)
                ) {
                    Text(
                        text = taggedTimer.tags.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
