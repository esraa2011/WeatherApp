package com.example.weatherapp.data.repo

import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository(
    var favoriteList: MutableList<FavoriteWeatherPlacesModel> =
        mutableListOf<FavoriteWeatherPlacesModel>(),

    var alertList: MutableList<AlarmPojo> = mutableListOf<AlarmPojo>(),
    var weatherResponse: Root = Root(
        46,
        30.0,
        32.0,
        "asdjadsk",
        565,
        null,
        emptyList(),
        emptyList(),
    )
) : RepositoryOperation {


    override fun getFavoritePlaces(): Flow<List<FavoriteWeatherPlacesModel>> = flow {
        emit(favoriteList)
    }


    override suspend fun insertFavoritePlaces(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        favoriteList.add(favoriteWeatherPlacesModel)
    }

    override suspend fun deleteFromFav(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        favoriteList.remove(favoriteWeatherPlacesModel)
    }

    override fun getAlert(): Flow<List<AlarmPojo>> = flow {
        emit(alertList)
    }

    override suspend fun insertAlert(alert: AlarmPojo) {
        alertList.add(alert)
    }

    override suspend fun deleteAlert(alert: AlarmPojo) {
        alertList.remove(alert)
    }

    override suspend fun getFavoriteWeather(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel): Flow<Root> =
        flow {
            emit(weatherResponse)
        }

    override suspend fun getRoot(latLng: LatLng): Flow<Root> = flow {
        emit(weatherResponse)
    }

    override fun getCurrentWeather(): Flow<Root> {
        TODO("Not yet implemented")
    }


    override fun insertCurrentWeather(root: Root) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeather() {
        TODO("Not yet implemented")
    }

    override suspend fun getWeather(latLng: LatLng): Flow<Root> {
        TODO("Not yet implemented")
    }


}