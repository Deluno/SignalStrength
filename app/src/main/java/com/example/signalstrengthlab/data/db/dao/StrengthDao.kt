package com.example.signalstrengthlab.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.signalstrengthlab.data.model.Strength
import kotlinx.coroutines.flow.Flow

@Dao
interface StrengthDao {
    @Query("SELECT * FROM strength ORDER BY measurement")
    fun getStrengths(): Flow<List<Strength>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(strength: List<Strength>)
}