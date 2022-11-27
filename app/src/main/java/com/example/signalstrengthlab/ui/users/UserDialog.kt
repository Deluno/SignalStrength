package com.example.signalstrengthlab.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.signalstrengthlab.R
import com.example.signalstrengthlab.data.model.User

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserDialog(
    modifier: Modifier = Modifier,
    user: User,
    onUserChange: (user: User) -> Unit,
    isEdit: Boolean = false,
    onPost: () -> Unit,
    onDelete: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(
            modifier = Modifier
                .height(1.5.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
        )
        OutlinedTextField(
            value = user.mac,
            onValueChange = { onUserChange(user.copy(mac = it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.mac_address)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                    focusManager.moveFocus(FocusDirection.Left)
                }
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            List(user.signals.size) { index ->
                val keyboardActions = if (index < user.signals.size - 1)
                    KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Right) }) else
                    KeyboardActions(onDone = { focusManager.clearFocus(); keyboard?.hide(); onPost() })

                val keyboardOptions = if (index < user.signals.size - 1)
                    KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Decimal
                    ) else
                    KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Decimal
                    )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = "${user.signals[index].strength}",
                    onValueChange = {
                        val mutableSignals = user.signals.toMutableList()
                        mutableSignals[index] = mutableSignals[index]
                            .copy(strength = it.toIntOrNull() ?: 0)
                        onUserChange(user.copy(signals = mutableSignals))
                    },
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions
                )
            }
        }
        Spacer(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(1.5.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (isEdit)
                Button(
                    onClick = { focusManager.clearFocus(); keyboard?.hide(); onDelete() },
                    modifier = Modifier
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    )
                ) {
                    Text(
                        text = "Delete",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            Button(
                onClick = { keyboard?.hide(); onPost() },
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "Post",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}