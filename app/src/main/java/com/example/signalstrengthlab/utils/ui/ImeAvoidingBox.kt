package com.example.signalstrengthlab.utils.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun ImeAvoidingBox(bottomPadding: Dp, content: @Composable BoxScope.() -> Unit) {
    val imeBottom = with(LocalDensity.current) {
        WindowInsets.ime.exclude(WindowInsets.navigationBars)
            .getBottom(this).toDp()
    }
    Box(
        Modifier.padding(bottom = (imeBottom).coerceAtLeast(bottomPadding)),
        content = content
    )
}