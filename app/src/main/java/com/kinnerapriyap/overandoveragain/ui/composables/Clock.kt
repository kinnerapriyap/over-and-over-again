package com.kinnerapriyap.overandoveragain.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Clock(
    time: Triple<Int, Int, Int>,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    showSeconds: Boolean = true,
    clockSize: Dp = 300.dp,
) {
    val (hours, minutes, seconds) = time
    Canvas(modifier = modifier.size(clockSize)) {
        val radius = size.width * .5f
        drawCircle(
            color = color,
            style = Stroke(width = 3f),
            radius = radius,
            center = size.center
        )
        drawCircle(
            color = color,
            radius = 6f,
            center = size.center
        )

        val hourAngle = ((minutes / 60.0 * 30.0) - 90.0 + (hours * 30.0)) * (PI / 180)
        drawLine(
            color = color,
            start = size.center,
            end = Offset(
                x = (radius * .5f) * cos(hourAngle.toFloat()) + size.center.x,
                y = (radius * .5f) * sin(hourAngle.toFloat()) + size.center.y
            ),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Square
        )

        val minutesAngle = ((seconds / 60.0 * 6.0) - 90.0 + (minutes * 6.0)) * (PI / 180)
        drawLine(
            color = color,
            start = size.center,
            end = Offset(
                x = (radius * .7f) * cos(minutesAngle.toFloat()) + size.center.x,
                y = (radius * .7f) * sin(minutesAngle.toFloat()) + size.center.y
            ),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Square
        )

        if (showSeconds) {
            val secondsAngle = ((seconds * 6.0) - 90.0) * (PI / 180)
            drawLine(
                color = color,
                start = size.center,
                end = Offset(
                    x = (radius * .9f) * cos(secondsAngle.toFloat()) + size.center.x,
                    y = (radius * .9f) * sin(secondsAngle.toFloat()) + size.center.y
                ),
                strokeWidth = 1.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}
