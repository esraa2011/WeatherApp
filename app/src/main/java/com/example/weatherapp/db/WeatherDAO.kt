package com.example.weatherapp.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.weatherapp.models.Root


@Dao
interface WeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertLastWeather(root: Root)

    @Query("SELECT * FROM Root")
   suspend fun getLastWeather(): Root

    @Query("DELETE FROM Root")
    suspend fun deleteCurrentWeather()

}