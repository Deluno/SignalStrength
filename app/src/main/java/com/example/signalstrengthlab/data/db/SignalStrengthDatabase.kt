package com.example.signalstrengthlab.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.signalstrengthlab.data.db.converters.Converters
import com.example.signalstrengthlab.data.db.dao.MeasurementDao
import com.example.signalstrengthlab.data.db.dao.StrengthDao
import com.example.signalstrengthlab.data.db.dao.UserDao
import com.example.signalstrengthlab.data.model.Measurement
import com.example.signalstrengthlab.data.model.Strength
import com.example.signalstrengthlab.data.model.User
import javax.inject.Singleton

@Database(
    entities = [
        Strength::class,
        Measurement::class,
        User::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SignalStrengthDatabase : RoomDatabase() {
    abstract val strengthDao: StrengthDao
    abstract val measurementDao: MeasurementDao
    abstract val userDao: UserDao
}