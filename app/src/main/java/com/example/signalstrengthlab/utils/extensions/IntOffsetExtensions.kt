package com.example.signalstrengthlab.utils.extensions

import androidx.compose.ui.unit.IntOffset

fun IntOffset.moveBy(offset: IntOffset): IntOffset {
    return this + offset
}