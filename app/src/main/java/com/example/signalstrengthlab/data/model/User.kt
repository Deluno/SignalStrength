package com.example.signalstrengthlab.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import javax.annotation.concurrent.Immutable

@Immutable
@Entity(tableName = "user")
@JsonClass(generateAdapter = true)
data class User(
    @PrimaryKey
    val mac: String = "",
    val signals: List<Signal> = listOf(
        Signal("wiliboxas1", 0),
        Signal("wiliboxas2", 0),
        Signal("wiliboxas3", 0)
    )
)