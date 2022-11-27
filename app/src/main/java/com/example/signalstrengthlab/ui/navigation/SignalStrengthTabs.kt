package com.example.signalstrengthlab.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.signalstrengthlab.R

enum class SignalStrengthTabs(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String,
) {
    MAPPING(R.string.mapping, Icons.Rounded.Home, SignalStrengthDestinations.MAPPING_ROUTE),
    USERS(R.string.users, Icons.Rounded.AccountCircle, SignalStrengthDestinations.USERS)
}