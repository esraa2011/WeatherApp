package com.example.weatherapp.repo

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.db.AppDataBase
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.models.Root
import com.example.weatherapp.models.Utility
import com.example.weatherapp.network.RetrofitHelper
import com.example.weatherapp.network.RetrofitInterface
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.log

class Repository(private val context: Context) {


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


    private val apiObject: RetrofitInterface =
        RetrofitHelper.getInstance().create(RetrofitInterface::class.java)

    suspend fun getRoot(latLng: LatLng): Flow<Root> = flow {


        updateSharedPreferance()
        if (location == Utility.GPS) {
            apiObject.getCurrentTempData(
                latLng.latitude,
                latLng.longitude,
                "bec88e8dd2446515300a492c3862a10e",
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

            apiObject.getCurrentTempData(
                latitude.toDouble(),
                longitude.toDouble(),
                "bec88e8dd2446515300a492c3862a10e",
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

    suspend fun getFavoriteWeather(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel): Flow<Root> =
        flow {
           updateSharedPreferance()
            apiObject.getCurrentTempData(
                favoriteWeatherPlacesModel.lat,
                favoriteWeatherPlacesModel.lon,
                "bec88e8dd2446515300a492c3862a10e",
                unit,
                language
            ).body().let {
                if (it != null) {
                    emit(it)
                }
            }

        }

    suspend fun insertFavoritePlaces(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        AppDataBase.getInstance(context).favoriteWeatherPlacesDAO()
            .addPlaceToFavorite(favoriteWeatherPlacesModel)
    }

    suspend fun deleteFromFav(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        AppDataBase.getInstance(context).favoriteWeatherPlacesDAO()
            .deletePlaceFromFavorite(favoriteWeatherPlacesModel)
    }

    fun getCurrentWeather() =
        AppDataBase.getInstance(context).weatherDAO().getLastWeather()

    fun getFavoritePlaces() =
        AppDataBase.getInstance(context).favoriteWeatherPlacesDAO()
            .getAllFavoriteWeatherPlaces()


    fun insertCurrentWeather(root: Root) {
        AppDataBase.getInstance(context).weatherDAO().insertLastWeather(root)
    }

    suspend fun deleteCurrentWeather() {
        AppDataBase.getInstance(context).weatherDAO().deleteCurrentWeather()
    }


    suspend fun getWeather(latLng: LatLng) = if (Utility.checkForInternet(context)) {
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

}