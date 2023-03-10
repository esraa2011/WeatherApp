package com.example.weatherapp.data.repo

import androidx.test.core.app.ApplicationProvider
import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import org.junit.Before
import org.junit.Test

class RepositoryTest {
    private var favoriteList: MutableList<FavoriteWeatherPlacesModel> =
        mutableListOf<FavoriteWeatherPlacesModel>(

            FavoriteWeatherPlacesModel("mansoura", 30.0, 32.0)
        )
    private var alertList: MutableList<AlarmPojo> = mutableListOf<AlarmPojo>(
        AlarmPojo(12, 13, "1", "2", "wind")
    )
    private var rootList: MutableList<Root> = mutableListOf<Root>(
        Root(1, 20.0, 32.0, "mansoura", null, null, null, null)

    )
    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource
    private lateinit var repository: Repository

    @Before
    fun initialzeRepository() {
        remoteDataSource = FakeDataSource(favoriteList, alertList, rootList)
        localDataSource = FakeDataSource(favoriteList, alertList, rootList)
        repository = Repository(
            remoteDataSource = remoteDataSource, localDataSource = localDataSource,
            ApplicationProvider.getApplicationContext()
        )
    }


    @Test
    fun getLanguageSharedPreferences() {
    }

    @Test
    fun setLanguageSharedPreferences() {
    }

    @Test
    fun getUnitsShared() {
    }

    @Test
    fun setUnitsShared() {
    }

    @Test
    fun getLanguage() {
    }

    @Test
    fun setLanguage() {
    }

    @Test
    fun getUnit() {
    }

    @Test
    fun setUnit() {
    }

    @Test
    fun getLocationSharedPreferences() {
    }

    @Test
    fun setLocationSharedPreferences() {
    }

    @Test
    fun getLocation() {
    }

    @Test
    fun setLocation() {
    }

    @Test
    fun getLatitudeSharedPreferences() {
    }

    @Test
    fun setLatitudeSharedPreferences() {
    }

    @Test
    fun getLongitudeSharedPreferences() {
    }

    @Test
    fun setLongitudeSharedPreferences() {
    }

    @Test
    fun getLatitude() {
    }

    @Test
    fun setLatitude() {
    }

    @Test
    fun getLongitude() {
    }

    @Test
    fun setLongitude() {
    }

    @Test
    fun updateSharedPreferance() {
    }

    @Test
    fun getRoot() {
    }

    @Test
    fun getFavoriteWeather() {
    }

    @Test
    fun insertFavoritePlaces() {
    }

    @Test
    fun deleteFromFav() {
    }

    @Test
    fun getCurrentWeather() {
    }

    @Test
    fun getFavoritePlaces() {
    }

    @Test
    fun insertCurrentWeather() {
    }

    @Test
    fun deleteCurrentWeather() {
    }

    @Test
    fun getWeather() {
    }

    @Test
    fun getAlert() {
    }

    @Test
    fun insertAlert() {
    }

    @Test
    fun deleteAlert() {
    }
}