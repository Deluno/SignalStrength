package com.example.signalstrengthlab.data.model

import com.squareup.moshi.JsonClass
import javax.annotation.concurrent.Immutable

@Immutable
@JsonClass(generateAdapter = true)
data class Signal(
    val sensor: String,
    val strength: Int
)