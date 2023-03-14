package com.example.weatherapp.data.db

import androidx.room.*
import com.example.weatherapp.data.models.AlarmPojo
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlarmPojo): Long

    @Query("SELECT * FROM AlarmPojo")
    fun getAllAlerts(): Flow<List<AlarmPojo>>

    @Query("DELETE FROM AlarmPojo WHERE id = :id")
    suspend fun deleteAlert(id: Int)

    @Query("SELECT * FROM AlarmPojo WHERE id = :id")
    suspend fun getAlert(id: Int): AlarmPojo
}