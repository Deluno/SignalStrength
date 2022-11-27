@file:OptIn(
    ExperimentalLifecycleComposeApi::class
)

package com.example.signalstrengthlab.ui.mapping

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.signalstrengthlab.data.model.Measurement
import com.example.signalstrengthlab.ui.misc.InfiniteLoadingIndicator
import com.example.signalstrengthlab.common.Resource

@Composable
fun MappingScreen(
    modifier: Modifier = Modifier,
    measurementsViewModel: MeasurementsViewModel = hiltViewModel()
) {
    val measurementsResource by measurementsViewModel.measurements.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        when (measurementsResource) {
            is Resource.Loading -> {
                InfiniteLoadingIndicator()
            }
            is Resource.Success -> {
                if (!measurementsResource.data.isNullOrEmpty())
                    MeasurementsMap(measurements = measurementsResource.data!!)
            }
            else -> {  }
        }
    }
}

@Composable
fun MeasurementsMap(
    modifier: Modifier = Modifier,
    measurements: List<Measurement>,
    usersPositionsViewModel: UsersPositionsViewModel = hiltViewModel()
) {
    val usersPositions by usersPositionsViewModel.getUsersPositions(measurements)
        .collectAsStateWithLifecycle(emptyMap())

    Box(modifier = modifier) {
        val minX = measurements
            .map { it.x }.minByOrNull { it } ?: 0
        val minY = measurements
            .map { it.y }.minByOrNull { it } ?: 0
        val maxX = measurements
            .map { it.x }.maxByOrNull { it } ?: 0
        val maxY = measurements
            .map { it.y }.maxByOrNull { it } ?: 0

        CanvasMapping(
            xRange = (minX..maxX),
            yRange = (minY..maxY),
            measuredLocations = measurements.map { IntOffset(it.x, it.y) },
            usersPositions = usersPositions.values.toList()
        )
    }
}