package ch.remodietlicher.andormu.ui

import android.content.res.Resources
import android.graphics.Paint
import android.util.TypedValue
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import ch.remodietlicher.andormu.data.TaggedTimer
import ch.remodietlicher.andormu.model.TaggedTimerViewModel
import java.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

private const val DEFAULT_NO_OF_HOURS = 5
private const val DEFAULT_NO_OF_MS = 1000 * 60 * 60 * DEFAULT_NO_OF_HOURS
private const val DEFAULT_NOW_PERCENTAGE = 0.8f

@OptIn(ExperimentalTextApi::class)
@Preview
@Composable
fun Timeline() {
    var offsetX by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offsetX += offsetChange.x
    }
    val viewModel: TaggedTimerViewModel = hiltViewModel()
    val timers: List<TaggedTimer> by viewModel.timers.collectAsState()
    var currentTime by remember { mutableStateOf(Date().time) }

    LaunchedEffect(true) {
        flow {
                while (true) {
                    emit(Date().time)
                    delay(1000)
                }
            }
            .collect { currentTime = it }
    }

    val screenWidthPx = Resources.getSystem().displayMetrics.widthPixels

    val pxPerMs = screenWidthPx * scale / DEFAULT_NO_OF_MS
    val msPerPx = 1 / pxPerMs
    val msPerHour = 1000 * 60 * 60

    val pxToTime = { px: Int -> (px * msPerPx).toLong() }

    val timeScreen = pxToTime(screenWidthPx)
    val timeLeft =
        currentTime - pxToTime((offsetX + DEFAULT_NOW_PERCENTAGE * screenWidthPx).toInt())
    val timeRight = timeLeft + timeScreen

    val timeToPx = { time: Long -> (time - timeLeft) * pxPerMs }

    Box(
        modifier =
            Modifier.fillMaxWidth().height(200.dp).transformable(state = state).drawBehind {
                // first full hour
                val timeFirstHour = (timeLeft + msPerHour - timeLeft % msPerHour)

                var t = timeFirstHour

                val lineHeight = size.height - 50.dp.toPx()

                while (t < timeRight) {
                    drawLine(
                        color = Color.White,
                        start = Offset(x = timeToPx(t), y = 0f),
                        end = Offset(x = timeToPx(t), y = lineHeight),
                        strokeWidth = 2f
                    )
                    // text with time below each line in HH:MM format
                    val calendar = Calendar.getInstance().apply { timeInMillis = t.toLong() }
                    val timeString =
                        "${calendar.get(Calendar.HOUR)}:${String.format("%02d", calendar.get(Calendar.MINUTE))}"

                    drawIntoCanvas {
                        val paint =
                            Paint().apply {
                                color = Color.White.toArgb()
                                textSize = 12.sp.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                        it.nativeCanvas.drawText(
                            timeString,
                            timeToPx(t),
                            lineHeight + 20.dp.toPx(),
                            paint,
                        )
                    }

                    t += msPerHour
                }
                drawLine(
                    color = Color.Red,
                    start = Offset(x = timeToPx(currentTime), y = 0f),
                    end = Offset(x = timeToPx(currentTime), y = lineHeight),
                    strokeWidth = 2f
                )
            }
    ) {
        // horizontal layout
        timers.forEachIndexed { index, taggedTimer ->
            val startTime = taggedTimer.startTime.time
            val endTime = taggedTimer.endTime?.time ?: currentTime
            val startX = timeToPx(startTime)
            val endX = timeToPx(endTime)
            val width = endX - startX

            val height = 50.dp

            // draw a button for each timer if within screen
            if (startTime < timeRight || endTime > timeLeft) {
                Button(
                    onClick = { /*TODO*/},
                    modifier =
                        Modifier.absoluteOffset(x = pxToDp(startX), y = index * height)
                            .size(width.dp, height)
                ) {
                    Text(text = taggedTimer.tag, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}
