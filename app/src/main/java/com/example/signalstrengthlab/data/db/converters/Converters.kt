package com.example.signalstrengthlab.data.db.converters

import androidx.room.TypeConverter
import com.example.signalstrengthlab.data.model.Signal
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType

class Converters {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun fromJsonList(value: String): List<Signal> {
        val type = Types.newParameterizedType(List::class.java, Signal::class.java)
        val adapter: JsonAdapter<List<Signal>> = moshi.adapter(type)
        return adapter.fromJson(value)!!
    }

    @TypeConverter
    fun toJsonList(list: List<Signal>): String {
        val type = Types.newParameterizedType(List::class.java, Signal::class.java)
        val adapter: JsonAdapter<List<Signal>> = moshi.adapter(type)
        return adapter.toJson(list)
    }
}