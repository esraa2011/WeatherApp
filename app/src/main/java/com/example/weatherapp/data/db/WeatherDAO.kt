package com.example.weatherapp.data.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.weatherapp.data.models.Root
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLastWeather(root: Root)

    @Query("SELECT * FROM Root")
    fun getLastWeather(): Flow<Root>

    @Query("DELETE FROM Root")
    suspend fun deleteCurrentWeather()

}