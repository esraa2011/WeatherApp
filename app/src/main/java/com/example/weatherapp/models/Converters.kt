package com.example.weatherapp.models

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromCurrentToString(current: Current) = Gson().toJson(current)

    @TypeConverter
    fun fromStringToCurrent(stringCurrent: String) =
        Gson().fromJson(stringCurrent, Current::class.java)

    @TypeConverter
    fun fromWeatherToString(weather: List<Weather>) = Gson().toJson(weather)

    @TypeConverter
    fun fromStringToWeather(stringCurrent: String) =
        Gson().fromJson(stringCurrent, Array<Weather>::class.java).toList()

    @TypeConverter
    fun fromweatherToString(weather: Weather) = Gson().toJson(weather)

    @TypeConverter
    fun fromStringToweather(stringCurrent: String) =
        Gson().fromJson(stringCurrent, Weather::class.java)

    @TypeConverter
    fun fromDailyListToString(daily: List<Daily>) = Gson().toJson(daily)

    @TypeConverter
    fun fromStringToDailyList(stringDaily: String) =
        Gson().fromJson(stringDaily, Array<Daily>::class.java).toList()

    @TypeConverter
    fun fromHourlyListToString(hourly: List<Current>) = Gson().toJson(hourly)

    @TypeConverter
    fun fromStringToHourlyList(stringHourly: String) =
        Gson().fromJson(stringHourly, Array<Current>::class.java).toList()


}