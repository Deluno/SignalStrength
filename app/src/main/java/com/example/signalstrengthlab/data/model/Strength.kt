package com.example.signalstrengthlab.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import javax.annotation.concurrent.Immutable

@Immutable
@Entity(tableName = "strength")
@JsonClass(generateAdapter = true)
data class Strength(
    @PrimaryKey
    val measurement: Int,
    val signals: List<Signal>
)