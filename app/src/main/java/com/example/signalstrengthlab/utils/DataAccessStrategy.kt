package com.example.signalstrengthlab.utils

import android.util.Log
import com.example.signalstrengthlab.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

fun <T, A> dbPriorityOperationFactory(
    databaseQuery: () -> Flow<A>,
    networkCall: suspend () -> Resource<T>,
    onSuccess: suspend (T) -> Unit
): suspend (
    localSourceCall: suspend (resource: Resource<A>) -> Unit,
    remoteSourceCall: suspend (resource: Resource<T>) -> Unit,
) -> Unit = { localCall, remoteCall ->
    coroutineScope {
        withContext(Dispatchers.IO) {
            databaseQuery()
                .map { Resource.Success(it) }
                .onEach { localCall(it) }
                .launchIn(this)
            flowOf(networkCall.invoke())
                .onEach { result ->
                    if (result is Resource.Success)
                        onSuccess(result.data!!)
                    remoteCall(result)
                }.launchIn(this)
        }
    }
}

fun <T, A> networkPriorityOperationFactory(
    databaseQuery: () -> Flow<A> = { emptyFlow() },
    networkCall: suspend () -> Resource<T>,
    onSuccess: suspend (T?) -> Unit,
): suspend (
    localSourceCall: suspend (resource: Resource<A>) -> Unit,
    remoteSourceCall: suspend (resource: Resource<T>) -> Unit,
) -> Unit = { localCall, remoteCall ->
    coroutineScope {
        withContext(Dispatchers.IO) {
            flowOf(networkCall.invoke())
                .onEach { res ->
                    if (res is Resource.Success)
                        onSuccess(res.data)
                    Log.d("networkPriority", "$res")
                    remoteCall(res)
                }.launchIn(this).invokeOnCompletion {
                    databaseQuery()
                        .map { Resource.Success(it) }
                        .onEach { localCall(it) }
                        .launchIn(this)
                }
        }
    }
}