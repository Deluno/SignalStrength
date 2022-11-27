package com.example.signalstrengthlab.ui.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun InfiniteLoadingIndicator(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    arrangement: Arrangement.Vertical = Arrangement.Center,
) {
    Column(
        horizontalAlignment = alignment,
        verticalArrangement = arrangement,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}