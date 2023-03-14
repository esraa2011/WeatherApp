package com.example.weatherapp.data.repo

import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import com.google.android.gms.maps.model.LatLng
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



    fun getAllAlerts(): Flow<List<AlarmPojo>>

    suspend fun insertAlert(alert: AlarmPojo): Long

    suspend fun deleteAlert(id: Int)

    suspend fun getAlert(id: Int): AlarmPojo
    fun insertLastWeather(root: Root)


    fun getLastWeather(): Flow<Root>

    suspend fun deleteCurrentWeather()

    fun getAllFavoriteWeatherPlaces(): Flow<List<FavoriteWeatherPlacesModel>>


    suspend fun addPlaceToFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel)

    suspend fun deletePlaceFromFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel)
    fun getWeatherAlert(latLng: LatLng): Flow<Root>
}