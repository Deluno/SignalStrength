package com.example.signalstrengthlab.ui.users

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.signalstrengthlab.data.model.User
import com.example.signalstrengthlab.data.model.Signal
import com.example.signalstrengthlab.app.theme.SignalStrengthTheme

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: User,
    userPosition: IntOffset?,
    onEditClick: () -> Unit = { },
) {
    var showContent by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .animateContentSize(TweenSpec(durationMillis = 500))
            .clickable { showContent = !showContent }
        ,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colors.primary),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.mac,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(12.dp, 12.dp),
                    color = MaterialTheme.colors.onPrimary
                )
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                }
            }
            if (showContent) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.DarkGray),
                ) {
                    user.signals.map {
                        UserStrengthItem(
                            modifier = Modifier.padding(12.dp, 4.dp),
                            signal = it,
                        )
                    }
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.DarkGray)
                ) {
                    UserPositionItem(
                        modifier = Modifier.padding(12.dp, 4.dp),
                        position = userPosition!!
                    )
                }
            }
        }
    }
}

@Composable
fun UserStrengthItem(modifier: Modifier = Modifier, signal: Signal) {
    Box(modifier = modifier) {
        Text(text = "${signal.sensor}  ->  strength: ${signal.strength}",)
    }
}

@Composable
fun UserPositionItem(modifier: Modifier = Modifier, position: IntOffset) {
    Box(modifier = modifier) {
        Text(text = "Position: (${position.x}; ${position.y})")
    }
}