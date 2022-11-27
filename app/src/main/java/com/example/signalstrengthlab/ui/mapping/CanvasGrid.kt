package com.example.signalstrengthlab.ui.mapping

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset

@Composable
fun CanvasGrid(
    modifier: Modifier = Modifier,
    xRange: IntRange,
    yRange: IntRange,
    setCellSize: DrawScope.() -> Size = { Size(5f, 5f) },
    setCellGap: DrawScope.() -> Offset = { Offset.Zero },
    setAxesOffset: DrawScope.() -> Offset = { Offset.Zero },
    centered: Boolean = false,
    onDrawCell: DrawScope.(gridPosition: IntOffset, topLeft: Offset, cellSize: Size) -> Unit,
    onDrawAxes: DrawScope.(topLeft: Offset, cellSize: Size, num: Int) -> Unit = { _, _, _ -> },
    vararg onRedrawCells: DrawScope.(gridPosition: IntOffset, topLeft: Offset, cellSize: Size) -> Unit
) {
    var canvasOffset = Offset.Zero
    val xRangeList = xRange.toList()
    val yRangeList = yRange.toList()
    val cols = xRange.count()
    val rows = yRange.count()

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val cellSize = setCellSize()
        val cellGap = setCellGap()
        val axesOffset = setAxesOffset()

        canvasOffset = if (centered)
            Offset(
                (size.width - cols * (cellSize.width + cellGap.x)) / 2,
                (size.height - rows * (cellSize.height + cellGap.y)) / 2
            ) else canvasOffset

        fun getCanvasCoordinates(mapPosition: IntOffset) =
            Offset(
                x = (cellSize.width + cellGap.x) * mapPosition.x + canvasOffset.x + cellGap.x / 2,
                y = -(cellSize.height + cellGap.y) * mapPosition.y + canvasOffset.y + cellGap.y / 2
            ) + Offset(0f, (rows - 1) * (cellSize.height + cellGap.y))

        fun iterateThroughCells(cellCallBack: (gridPosition: IntOffset, topLeft: Offset) -> Unit) {
            for (col in (0 until cols)) {
                for (row in (0 until rows)) {
                    val topLeft = getCanvasCoordinates(IntOffset(col, row))
                    cellCallBack(IntOffset(xRangeList[col],yRangeList[row]), topLeft)
                }
            }
        }

        iterateThroughCells { gridPosition, topLeft ->
            onDrawCell(gridPosition, topLeft, cellSize)
        }

        for (col in (0 until cols)) {
            val position = getCanvasCoordinates(IntOffset(col, 0))
            val topLeft = Offset(
                x = position.x,
                y = position.y + cellSize.height + axesOffset.y
            )
            onDrawAxes(topLeft, cellSize, xRangeList[col])
        }

        for (row in (0 until rows)) {
            val position = getCanvasCoordinates(IntOffset(0, row))
            val topLeft = Offset(
                x = position.x - cellSize.width - axesOffset.x,
                y = position.y
            )
            onDrawAxes(topLeft, cellSize, yRangeList[row])
        }

        for (redraw in onRedrawCells) {
            iterateThroughCells { gridPosition, topLeft ->
                redraw(gridPosition, topLeft, cellSize)
            }
        }
    }
}