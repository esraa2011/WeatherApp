package com.example.weatherapp.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import com.example.weatherapp.db.AppDataBase
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.Daily
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.models.Root
import com.example.weatherapp.network.RetrofitHelper
import com.example.weatherapp.network.RetrofitInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class Repository(private val context: Context) {

    private val apiObject: RetrofitInterface =
        RetrofitHelper.getInstance().create(RetrofitInterface::class.java)

    suspend fun getRoot(): Flow<Root> = flow {
        apiObject.getCurrentTempData(32.0, 31.0, "bec88e8dd2446515300a492c3862a10e", "metric", "en")
            .body().let {
                if (it != null) {
                    emit(it)
                }
            }
    }

    suspend fun getFavoriteWeather(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel): Flow<Root> =
        flow {
            apiObject.getCurrentTempData(
                favoriteWeatherPlacesModel.lat,
                favoriteWeatherPlacesModel.lon,
                "bec88e8dd2446515300a492c3862a10e",
                "metric",
                "en"
            ).body().let {
                if (it != null) {
                    emit(it)
                }
            }

        }

    suspend fun insertFavoritePlaces(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        AppDataBase.getInstance(context)?.favoriteWeatherPlacesDAO()
            ?.addPlaceToFavorite(favoriteWeatherPlacesModel)
    }

    suspend fun deleteFromFav(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        AppDataBase.getInstance(context)?.favoriteWeatherPlacesDAO()
            ?.deletePlaceFromFavorite(favoriteWeatherPlacesModel)
    }

    fun getCurrentWeather() =
        AppDataBase.getInstance(context)?.weatherDAO()?.getLastWeather()

    fun getFavoritePlaces() =
        AppDataBase.getInstance(context).favoriteWeatherPlacesDAO()
            .getAllFavoriteWeatherPlaces()


    fun insertCurrentWeather(root: Root) {
        AppDataBase.getInstance(context)?.weatherDAO()?.insertLastWeather(root)
    }

    suspend fun deleteCurrentWeather() {
        AppDataBase.getInstance(context)?.weatherDAO()?.deleteCurrentWeather()
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION") return networkInfo.isConnected
        }
    }

    suspend fun getWeather() = if (checkForInternet(context)) {
        getRoot().also {
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