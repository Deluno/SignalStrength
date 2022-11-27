package com.example.signalstrengthlab.ui.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.signalstrengthlab.common.Resource
import com.example.signalstrengthlab.data.model.Measurement
import com.example.signalstrengthlab.data.model.User
import com.example.signalstrengthlab.ui.mapping.MeasurementsViewModel
import com.example.signalstrengthlab.ui.mapping.UsersPositionsViewModel
import com.example.signalstrengthlab.ui.misc.InfiniteLoadingIndicator

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun UsersContent(
    modifier: Modifier = Modifier,
    usersResource: Resource<List<User>>,
    onEditUserClick: (user: User) -> Unit,
    measurementsViewModel: MeasurementsViewModel = hiltViewModel()
) {
    val measurementsResource by measurementsViewModel.measurements.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (usersResource is Resource.Loading || measurementsResource is Resource.Loading)
            InfiniteLoadingIndicator()
        if (usersResource is Resource.Success && measurementsResource is Resource.Success)
            UsersList(
                users = usersResource.data!!,
                measurements = measurementsResource.data!!,
                onEditUserClick = { user -> onEditUserClick(user) }
            )
    }
}

@ExperimentalLifecycleComposeApi
@Composable
fun UsersList(
    modifier: Modifier = Modifier,
    users: List<User>,
    measurements: List<Measurement>,
    onEditUserClick: (user: User) -> Unit = {},
    usersPositionsViewModel: UsersPositionsViewModel = hiltViewModel()
) {
    val usersPositions by usersPositionsViewModel.getUsersPositions(measurements)
        .collectAsStateWithLifecycle(emptyMap())

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
        ) {
            items(users) { user ->
                UserItem(
                    user = user,
                    userPosition = usersPositions[user],
                    modifier = Modifier.padding(8.dp, 4.dp),
                    onEditClick = { onEditUserClick(user) }
                )
            }
        }
    }
}