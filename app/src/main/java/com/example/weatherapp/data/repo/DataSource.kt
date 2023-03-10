package com.example.weatherapp.data.repo

import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DataSource {


    suspend fun getCurrentTempData(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Response<Root>

    suspend fun insertAlert(alert: AlarmPojo)

    fun getAlert(): Flow<List<AlarmPojo>>

    suspend fun deleteAlert(alert: AlarmPojo)


    fun insertLastWeather(root: Root)


    fun getLastWeather(): Flow<Root>

    suspend fun deleteCurrentWeather()

    fun getAllFavoriteWeatherPlaces(): Flow<List<FavoriteWeatherPlacesModel>>


    suspend fun addPlaceToFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel)

    suspend fun deletePlaceFromFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel)

}