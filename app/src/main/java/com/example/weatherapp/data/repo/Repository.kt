package com.example.weatherapp.data.repo

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.data.db.AppDataBase
import com.example.weatherapp.data.db.LocalDataSource
import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import com.example.weatherapp.data.models.Utility
import com.example.weatherapp.data.network.RemoteDataSource
import com.example.weatherapp.data.network.RetrofitHelper
import com.example.weatherapp.data.network.RetrofitInterface
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository(

    private val remoteDataSource: DataSource,
    private val localDataSource: DataSource,
    private val context: Context
) : RepositoryOperation {

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null
        fun getRepository(app: Application): Repository {
            return INSTANCE ?: synchronized(this) {
                val room = AppDataBase.getInstance(app)
                val retrofit = RetrofitHelper.getInstance()
                val api = retrofit.create(RetrofitInterface::class.java)
                val localDataSource = LocalDataSource(
                    favoriteWeatherPlacesDAO = room.favoriteWeatherPlacesDAO(),
                    alertDAO = room.AlertDAO(),
                    weatherDAO = room.weatherDAO()
                )
                val remoteDataSource = RemoteDataSource(api = api)
                Repository(
                    remoteDataSource = remoteDataSource,
                    localDataSource = localDataSource,
                    app.applicationContext
                )
            }
        }
    }

    lateinit var languageSharedPreferences: SharedPreferences
    lateinit var unitsShared: SharedPreferences
    lateinit var language: String
    lateinit var unit: String
    lateinit var locationSharedPreferences: SharedPreferences
    lateinit var location: String
    lateinit var latitudeSharedPreferences: SharedPreferences
    lateinit var longitudeSharedPreferences: SharedPreferences
    var latitude: Long = 32
    var longitude: Long = 30


    fun updateSharedPreferance() {
        languageSharedPreferences =
            context.getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)

        unitsShared = context.getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)

        locationSharedPreferences =
            context.getSharedPreferences(Utility.LOCATION_KEY, Context.MODE_PRIVATE)

        longitudeSharedPreferences =
            context.getSharedPreferences(Utility.LONGITUDE_KEY, Context.MODE_PRIVATE)

        latitudeSharedPreferences =
            context.getSharedPreferences(Utility.LATITUDE_KEY, Context.MODE_PRIVATE)

        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!

        unit = unitsShared.getString(Utility.TEMP_KEY, "metric")!!

        location = locationSharedPreferences.getString(Utility.LOCATION_KEY, Utility.GPS)!!

        latitude = latitudeSharedPreferences.getLong(Utility.LATITUDE_KEY, 32.toLong())!!

        longitude = longitudeSharedPreferences.getLong(Utility.LONGITUDE_KEY, 30.toLong())!!

    }


    override suspend fun getRoot(latLng: LatLng): Flow<Root> = flow {


        updateSharedPreferance()
        if (location == Utility.GPS) {
            remoteDataSource.getCurrentTempData(
                latLng.latitude,
                latLng.longitude,
                "d9abb2c1d05c5882e937cffd1ecd4923",
                unit,
                language
            )
                .body().let {
                    if (it != null) {
                        emit(it)
                    }
                }

        } else {
            Log.i("esraa", "getRoot: 12345")

            remoteDataSource.getCurrentTempData(
                latitude.toDouble(),
                longitude.toDouble(),
                "d9abb2c1d05c5882e937cffd1ecd4923",
                unit,
                language
            )
                .body().let {
                    if (it != null) {
                        emit(it)
                    }
                }
        }

    }

    override suspend fun getFavoriteWeather(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel): Flow<Root> =
        flow {
            updateSharedPreferance()
            remoteDataSource.getCurrentTempData(
                favoriteWeatherPlacesModel.lat,
                favoriteWeatherPlacesModel.lon,
                "d9abb2c1d05c5882e937cffd1ecd4923",
                unit,
                language
            ).body().let {
                if (it != null) {
                    emit(it)
                }
            }

        }

    override suspend fun insertFavoritePlaces(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        localDataSource
            .addPlaceToFavorite(favoriteWeatherPlacesModel)
    }

    override suspend fun deleteFromFav(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        localDataSource
            .deletePlaceFromFavorite(favoriteWeatherPlacesModel)
    }

    override fun getCurrentWeather() =
        localDataSource.getLastWeather()

    override fun getFavoritePlaces() =
        localDataSource
            .getAllFavoriteWeatherPlaces()


    override fun insertCurrentWeather(root: Root) {
        localDataSource.insertLastWeather(root)
    }

    override suspend fun deleteCurrentWeather() {
        localDataSource.deleteCurrentWeather()
    }


    override suspend fun getWeather(latLng: LatLng) = if (Utility.checkForInternet(context)) {
        getRoot(latLng).also {
            deleteCurrentWeather()

            it.collect {
                if (it != null) {
                    insertCurrentWeather(it)
                }
            }

        }
    } else {

        getCurrentWeather()

    }

    override fun getAlert() =
        localDataSource
            .getAlert()

    override suspend fun insertAlert(alert: AlarmPojo) {
        localDataSource
            .insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlarmPojo) {
        localDataSource
            .deleteAlert(alert)
    }
}