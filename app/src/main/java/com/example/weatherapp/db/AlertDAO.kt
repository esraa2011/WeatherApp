package com.example.weatherapp.db

import androidx.room.*
import com.example.weatherapp.models.AlarmPojo
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAlert(alert: AlarmPojo)

    @Query("SELECT * FROM AlarmPojo")
    fun getAlert(): Flow<List<AlarmPojo>>

    @Delete
   suspend fun deleteAlert(alert: AlarmPojo)
}