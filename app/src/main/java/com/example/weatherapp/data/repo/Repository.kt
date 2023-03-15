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

        fun getInstance(context: Context): Repository {
            return INSTANCE ?: synchronized(this) {
                val room = AppDataBase.getInstance(context)
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
                    context
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
                "698e3af38374a53ef1b6cc63e337b71c",
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
                "698e3af38374a53ef1b6cc63e337b71c",
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
                "698e3af38374a53ef1b6cc63e337b71c",
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

    override fun getAllAlerts(): Flow<List<AlarmPojo>> {
        return localDataSource.getAllAlerts()
    }

    override suspend fun insertAlert(alert: AlarmPojo): Long {
        return localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(id: Int) {
        localDataSource.deleteAlert(id)
    }

    override suspend fun getAlert(id: Int): AlarmPojo {
        return localDataSource.getAlert(id)
    }

    override suspend fun getWeatherAlert(latLng: LatLng): Flow<Root> = flow {
        updateSharedPreferance()
        remoteDataSource.getCurrentTempData(
            latLng.latitude,
            latLng.longitude,

            //"44c59959fbe6086cb77fb203967bbc0c",
            //          "bec88e8dd2446515300a492c3862a10e",
            //"d9abb2c1d05c5882e937cffd1ecd4923",
            // "4a059725f93489b95183bbcb8c6829b9",
            //  "f112a761188e9c22cdf3eb3a44597b00",
            "698e3af38374a53ef1b6cc63e337b71c",
                    unit,
            language
        ).body().let {
            if (it != null) {
                emit(it)
            }
        }
    }

}