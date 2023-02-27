package com.example.weatherapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.models.Converters
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.models.Root


@Database(entities = [Root::class, FavoriteWeatherPlacesModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteWeatherPlacesDAO(): FavoriteWeatherPlacesDAO
    abstract fun weatherDAO(): WeatherDAO

    companion object {
        private var INSTANCE: AppDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBase? {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDataBase::class.java, "FavoritePlaces_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }

        }
    }

}