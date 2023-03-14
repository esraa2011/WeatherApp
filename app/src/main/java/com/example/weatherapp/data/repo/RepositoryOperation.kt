package com.example.weatherapp.data.repo

import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface RepositoryOperation {
    suspend fun getRoot(latLng: LatLng): Flow<Root>

    suspend fun getFavoriteWeather(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel): Flow<Root>

    suspend fun insertFavoritePlaces(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel)

    suspend fun deleteFromFav(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel)
    fun getCurrentWeather(): Flow<Root>
    fun getFavoritePlaces(): Flow<List<FavoriteWeatherPlacesModel>>
    fun insertCurrentWeather(root: Root)

    suspend fun deleteCurrentWeather()

    suspend fun getWeather(latLng: LatLng): Flow<Root>

    suspend fun deleteAlert(id: Int)
    suspend fun insertAlert(alert: AlarmPojo): Long
    fun getAllAlerts(): Flow<List<AlarmPojo>>
    suspend fun getAlert(id: Int): AlarmPojo
    suspend fun getWeatherAlert(latLng: LatLng): Flow<Root>


}