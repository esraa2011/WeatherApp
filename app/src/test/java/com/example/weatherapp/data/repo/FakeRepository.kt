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
        emptyList()
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

    override suspend fun deleteAlert(id: Int) {
        alertList.remove(alertList[id])
    }

    override suspend fun insertAlert(alert: AlarmPojo): Long {
        alertList.add(alert)
        return alert.id?.toLong() ?: 0
    }

    override fun getAllAlerts(): Flow<List<AlarmPojo>> = flow {
        emit(alertList)
    }

    override suspend fun getAlert(id: Int): AlarmPojo {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherAlert(latLng: LatLng): Flow<Root> {
        TODO("Not yet implemented")
    }


}