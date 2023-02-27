package com.example.weatherapp.db


import androidx.room.*
import com.example.weatherapp.models.FavoriteWeatherPlacesModel

@Dao
interface FavoriteWeatherPlacesDAO {
    @Query("SELECT * FROM FavoriteWeatherPlacesModel ")
    suspend fun getAllFavoriteWeatherPlaces(): MutableList<FavoriteWeatherPlacesModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaceToFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel)

    @Delete
    suspend fun deletePlaceFromFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel)


}