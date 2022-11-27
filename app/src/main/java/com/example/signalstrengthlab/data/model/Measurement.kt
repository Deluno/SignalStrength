package com.example.signalstrengthlab.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import javax.annotation.concurrent.Immutable

@Immutable
@Entity(tableName = "measurement")
@JsonClass(generateAdapter = true)
data class Measurement(
    @PrimaryKey
    val id: Int,
    val x: Int,
    val y: Int,
    val signals: List<Signal>
)