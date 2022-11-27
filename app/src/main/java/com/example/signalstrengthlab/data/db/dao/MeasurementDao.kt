package com.example.signalstrengthlab.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.signalstrengthlab.data.model.Measurement
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {
    @Query("SELECT * FROM measurement")
    fun getMeasurements(): Flow<List<Measurement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(measurement: List<Measurement>)
}