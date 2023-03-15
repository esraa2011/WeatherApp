package com.example.weatherapp.data.repo

import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeDataSource(

    var favoriteList: MutableList<FavoriteWeatherPlacesModel> = mutableListOf<FavoriteWeatherPlacesModel>(),
    private var alertList: MutableList<AlarmPojo> = mutableListOf<AlarmPojo>(),
    private var rootList: MutableList<Root> = mutableListOf<Root>(),
) : DataSource {
    override suspend fun getCurrentTempData(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Response<Root> {
        return Response.success(
            Root(1, 20.0, 32.0, "mansoura", 55, null, emptyList(), emptyList(), emptyList())
        )
    }


    override fun getAllAlerts(): Flow<List<AlarmPojo>> = flow {
        emit(alertList)
    }

    override suspend fun deleteAlert(id: Int) {
        alertList.remove(alertList[id])
    }

    override suspend fun getAlert(id: Int): AlarmPojo {
        return alertList[id]
    }

    override suspend fun insertAlert(alert: AlarmPojo): Long {
        alertList.add(alert)
        return alert.id?.toLong() ?: 0
    }

    override fun insertLastWeather(root: Root) {
        rootList.add(root)
    }

    override fun getLastWeather(): Flow<Root> = flow {
        if (rootList.isEmpty()) {
            emit(Root(1, 20.0, 32.0, "mansoura", 55, null, emptyList(), emptyList(), emptyList()))
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

    override fun getWeatherAlert(latLng: LatLng): Flow<Root> {
        TODO("Not yet implemented")
    }


}