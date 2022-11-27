package com.example.signalstrengthlab.ui.mapping

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.example.signalstrengthlab.app.theme.DarkGray
import com.example.signalstrengthlab.app.theme.Orange
import com.example.signalstrengthlab.utils.extensions.drawCell

@Composable
fun CanvasMapping(
    xRange: IntRange,
    yRange: IntRange,
    measuredLocations: List<IntOffset>,
    usersPositions: List<IntOffset?>
    ) {
    val offsetSaver = listSaver<Offset, Any>(
        save = { listOf(it.x, it.y) },
        restore = { Offset(it[0] as Float, it[1] as Float) }
    )
    var scale by rememberSaveable { mutableStateOf(1f) }
    var offset by rememberSaveable(stateSaver = offsetSaver) { mutableStateOf(Offset.Zero) }

    val colors = MaterialTheme.colors

    CanvasGrid(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, gestureZoom, _ ->
                    val oldScale = scale
                    val newScale = (scale * gestureZoom).coerceIn(.25f, 2f)

                    offset = (offset + centroid / oldScale) -
                            (centroid / newScale + pan / oldScale)
                    scale = newScale
                }
            }
            .graphicsLayer {
                translationX = -offset.x * scale
                translationY = -offset.y * scale
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0f, 0f)
            },
        xRange = xRange,
        yRange = yRange,
        centered = true,
        setCellSize = { Size(32.dp.toPx(), 32.dp.toPx()) },
        setCellGap = { Offset(4.dp.toPx(), 4.dp.toPx()) },
        setAxesOffset = { Offset(16.dp.toPx(), 16.dp.toPx()) },
        onDrawCell = { _, topLeft, cellSize ->
            drawCell(
                topLeft = topLeft,
                size = cellSize,
                color = DarkGray,
                text = "0",
                textColor = colors.onPrimary,
                textSize = 20.sp
            )
        },
        onDrawAxes = { topLeft, cellSize, num ->
            drawCell(
                topLeft = topLeft,
                size = cellSize,
                color = colors.primaryVariant,
                text = "$num",
                textColor = colors.onPrimary,
                textSize = 20.sp
            )
        },
        onRedrawCells = arrayOf(
            { gridPosition, topLeft, cellSize ->
                if (gridPosition in measuredLocations) {
                    drawCell(
                        topLeft = topLeft,
                        size = cellSize,
                        color = colors.primary,
                        text = "1",
                        textColor = colors.onPrimary,
                        textSize = 20.sp
                    )
                }
            },
            { gridPosition, topLeft, cellSize ->
                if (gridPosition in usersPositions) {
                    val countUsersPositions = usersPositions.count { it == gridPosition }
                    drawCell(
                        topLeft = topLeft,
                        size = cellSize,
                        color = colors.secondary,
                        text = "$countUsersPositions",
                        textColor = colors.onPrimary,
                        textSize = 20.sp
                    )
                }
            }
        )
    )
}