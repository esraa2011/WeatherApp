package com.example.weatherapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Root")
data class Root(
    @PrimaryKey
    var id: Int,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    var current: Current?=null,
    var hourly: List<Current> ,
    var daily: List<Daily>,
    var alerts: List<Alerts>?

)

data class Current(
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
    val pop: Double? = null
)

data class Weather(
    val id: Long,
    val main: Main,
    val description: String,
    val icon: String
)

enum class Description {
    BrokenClouds,
    ClearSky,
    FewClouds,
    OvercastClouds,
    ScatteredClouds
}

enum class Icon {
    The01D,
    The01N,
    The02D,
    The02N,
    The03D,
    The03N,
    The04N
}

enum class Main {
    Clear,
    Clouds
}

data class Daily(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moonPhase: Double,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
    val clouds: Long,
    val pop: Double,
    val uvi: Double
)

data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)


data class Alerts(
    var senderName: String? = null,
    var event: String? = null,
     var start: Long? = null,
     var end: Long? = null,
     var description: String? = null,
     var tags: List<String>
)
