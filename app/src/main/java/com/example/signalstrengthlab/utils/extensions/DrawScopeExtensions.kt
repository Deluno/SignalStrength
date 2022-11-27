package com.example.signalstrengthlab.utils.extensions

import android.graphics.Paint
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun DrawScope.drawCell(
    topLeft: Offset,
    size: Size,
    text: String = "",
    color: Color = Color.Black,
    textColor: Color = Color.White,
    textSize: TextUnit = 10.sp
) {
    drawRoundRect(
        color = color,
        topLeft = topLeft,
        size = size,
        cornerRadius = CornerRadius(4.dp.toPx())
    )

    val textPaint = Paint().apply {
        this.color = textColor.toArgb()
        this.textSize = textSize.toPx()
        isFakeBoldText = true
    }
    val textWidth = textPaint.measureText(text) / 2
    val textPaintSize = textPaint.textSize

    drawIntoCanvas {
        val rect = Rect(
            topLeft.x - textWidth,
            topLeft.y + textPaintSize / 1.4f,
            topLeft.x + size.width + textWidth,
            topLeft.y + size.height
        )
        it.nativeCanvas.drawText(
            text,
            rect.center.x,
            rect.center.y,
            textPaint.apply {
                textAlign = Paint.Align.CENTER
            }
        )
    }
}