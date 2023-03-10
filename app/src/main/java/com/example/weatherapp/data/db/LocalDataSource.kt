package com.example.weatherapp.data.db

import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import com.example.weatherapp.data.repo.DataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class LocalDataSource(
    private val alertDAO: AlertDAO,
    private val weatherDAO: WeatherDAO,
    private val favoriteWeatherPlacesDAO: FavoriteWeatherPlacesDAO
) : DataSource {
    override suspend fun getCurrentTempData(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Response<Root> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: AlarmPojo) {
        alertDAO.insertAlert(alert)
    }

    override fun getAlert(): Flow<List<AlarmPojo>> {
        return alertDAO.getAlert()
    }

    override suspend fun deleteAlert(alert: AlarmPojo) {
        alertDAO.deleteAlert(alert)
    }

    override fun insertLastWeather(root: Root) {
        weatherDAO.insertLastWeather(root)
    }

    override fun getLastWeather(): Flow<Root> {
        return weatherDAO.getLastWeather()
    }

    override suspend fun deleteCurrentWeather() {
        weatherDAO.deleteCurrentWeather()
    }

    override fun getAllFavoriteWeatherPlaces(): Flow<List<FavoriteWeatherPlacesModel>> {
        return favoriteWeatherPlacesDAO.getAllFavoriteWeatherPlaces()
    }

    override suspend fun addPlaceToFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        favoriteWeatherPlacesDAO.addPlaceToFavorite(favoriteWeatherPlacesModel)
    }

    override suspend fun deletePlaceFromFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        favoriteWeatherPlacesDAO.deletePlaceFromFavorite(favoriteWeatherPlacesModel)
    }

}