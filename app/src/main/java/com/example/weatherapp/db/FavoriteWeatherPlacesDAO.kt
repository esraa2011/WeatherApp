package com.example.weatherapp.db


import androidx.room.*
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteWeatherPlacesDAO {
    @Query("SELECT * FROM FavoriteWeatherPlacesModel ")
   fun getAllFavoriteWeatherPlaces(): Flow<List<FavoriteWeatherPlacesModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaceToFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel)

    @Delete
    suspend fun deletePlaceFromFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel)


}