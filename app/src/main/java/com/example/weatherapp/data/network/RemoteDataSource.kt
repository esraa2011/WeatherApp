package com.example.weatherapp.data.network

import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import com.example.weatherapp.data.repo.DataSource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemoteDataSource(

    private val api: RetrofitInterface

) : DataSource {

    override suspend fun getCurrentTempData(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Response<Root> {
        return api.getCurrentTempData(
            latitude,
            longitude,
            appid,
            units,
            lang
        )
    }

    override fun getAllAlerts(): Flow<List<AlarmPojo>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: AlarmPojo): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlert(id: Int): AlarmPojo {
        TODO("Not yet implemented")
    }


    override fun insertLastWeather(root: Root) {
        TODO("Not yet implemented")
    }

    override fun getLastWeather(): Flow<Root> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeather() {
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteWeatherPlaces(): Flow<List<FavoriteWeatherPlacesModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun addPlaceToFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlaceFromFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        TODO("Not yet implemented")
    }

    override fun getWeatherAlert(latLng: LatLng): Flow<Root> {
        TODO("Not yet implemented")
    }
}