package com.example.signalstrengthlab.utils

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchAll(
    context: CoroutineContext = Dispatchers.Default,
    start: CoroutineStart = CoroutineStart.LAZY,
    vararg blocks: suspend CoroutineScope.() -> Unit
): List<Job> {
    return blocks.map { launch(context, start) { it() } }
}