@file:OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalLifecycleComposeApi::class,
)

package com.example.signalstrengthlab.ui.users

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.signalstrengthlab.data.model.User
import com.example.signalstrengthlab.utils.ui.Keyboard
import com.example.signalstrengthlab.utils.ui.keyboardAsState
import kotlinx.coroutines.launch


@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    usersViewModel: UsersViewModel = hiltViewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val usersResource by usersViewModel.users.collectAsStateWithLifecycle()

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(8.dp, 8.dp),
        sheetPeekHeight = 40.dp,
        sheetContent = {
            val handlePostAction: () -> Unit = {
                if (userViewModel.isEditUser)
                    usersViewModel.updateUser(
                        userViewModel.userReference.mac,
                        userViewModel.userState
                    ) else
                    usersViewModel.addUser(userViewModel.userState)
                scope.launch {
                    scaffoldState.bottomSheetState.collapse()
                }
                userViewModel.resetState()
            }
            val handleDeleteAction: () -> Unit = {
                usersViewModel.deleteUser(userViewModel.userReference.mac)
                scope.launch {
                    scaffoldState.bottomSheetState.collapse()
                }
                userViewModel.resetState()
            }

            Column{
                Spacer(modifier = Modifier.height(25.dp))
                UserDialog(
                    isEdit = userViewModel.isEditUser,
                    user = userViewModel.userState,
                    onUserChange = { user -> userViewModel.changeUserState(user) },
                    onPost = handlePostAction,
                    onDelete = handleDeleteAction
                )

            }
        },
        floatingActionButton = {
            val handleBottomSheetButtonClick: () -> Unit = {
                scope.launch {
                    scaffoldState.bottomSheetState.apply {
                        if (!isCollapsed) collapse()
                        else {
                            userViewModel.switchToAddUserState()
                            expand()
                        }
                    }
                }
            }

            BottomSheetActionButton(
                modifier = Modifier.padding(bottom = 50.dp),
                scaffoldState = scaffoldState,
                onClick = handleBottomSheetButtonClick
            )
        },
    ) { pd ->
        val handleUserEdit: (user: User) -> Unit = {
            scope.launch {
                scaffoldState.bottomSheetState.apply {
                    userViewModel.switchToEditUserState(it)
                    if (isCollapsed) expand()
                }
            }
        }

        UsersContent(
            modifier = modifier.padding(pd),
            usersResource = usersResource,
            onEditUserClick = { handleUserEdit(it) }
        )
    }
}

@Composable
private fun BottomSheetActionButton(
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState,
    onClick: () -> Unit = {},
) {
    var rotated: Boolean by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (rotated) -45F else 0F,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(scaffoldState.bottomSheetState.targetValue) {
        rotated = scaffoldState.bottomSheetState.targetValue != BottomSheetValue.Collapsed
    }

    AddUserButton(iconModifier = Modifier.rotate(angle), onClick = onClick)
}

@Composable
fun AddUserButton(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(50.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            modifier = iconModifier.size(32.dp),
        )
    }
}