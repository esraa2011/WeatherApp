package com.example.weatherapp.data.repo

import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeDataSource(
    private var favoriteList: MutableList<FavoriteWeatherPlacesModel> = mutableListOf<FavoriteWeatherPlacesModel>(),
    private var alertList: MutableList<AlarmPojo> = mutableListOf<AlarmPojo>(),
    private var rootList: MutableList<Root> = mutableListOf<Root>()
) : DataSource {

    override suspend fun getCurrentTempData(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Response<Root> {
        return Response.success(
            Root()
        )
    }

    override suspend fun insertAlert(alert: AlarmPojo) {
        alertList.add(alert)

    }

    override fun getAlert(): Flow<List<AlarmPojo>> = flow {
        emit(alertList)
    }

    override suspend fun deleteAlert(alert: AlarmPojo) {
        alertList.remove(alert)
    }

    override fun insertLastWeather(root: Root) {
        rootList.add(root)
    }

    override fun getLastWeather(): Flow<Root> = flow {
        if (rootList.isEmpty()) {
            emit(Root())
        }
        emit(rootList.get(0))
    }

    override suspend fun deleteCurrentWeather() {
        rootList.clear()
    }

    override fun getAllFavoriteWeatherPlaces(): Flow<List<FavoriteWeatherPlacesModel>> = flow {
        emit(favoriteList)
    }

    override suspend fun addPlaceToFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        favoriteList.add(favoriteWeatherPlacesModel)
    }

    override suspend fun deletePlaceFromFavorite(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        favoriteList.remove(favoriteWeatherPlacesModel)
    }

}