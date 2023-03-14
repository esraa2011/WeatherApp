package com.example.weatherapp.data.models

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utility {

    companion object {

        const val latLongSharedPrefKey: String = "LatLong"
        const val GPSLatKey: String = "GPSLat"
        const val GPSLongKey: String = "GPSLong"
        const val Language_EN_Value: String = "en"
        const val Language_AR_Value: String = "ar"
        const val Language_Key: String = "Lang"
        const val Language_Value_Key = "Language"
        const val TEMP_KEY = "Temp"
        const val IMPERIAL = "imperial"
        const val STANDARD = "standard"
        const val METRIC = "metric"
        const val LOCATION_KEY = "location"
        const val MAP = "map"
        const val GPS = "gps"
        const val LATITUDE_KEY = "Latitude"
        const val LONGITUDE_KEY = "Longitude"
        const val NOTIFICATION_KEY: String = "Notification"
        const val notificationEnable: String = "enable"
        const val notificationDis: String = "disable"


        fun timeStampToDay(dt: Long): String {
            var date: Date = Date(dt * 1000)
            var dateFormat: DateFormat = SimpleDateFormat("EEEE")
            return dateFormat.format(date)
        }

        fun getWeatherIcon(imageString: String): Int {
            val imageInInteger: Int
            when (imageString) {
                "01d" -> imageInInteger = R.drawable.icon_01d
                "01n" -> imageInInteger = R.drawable.icon_01n
                "02d" -> imageInInteger = R.drawable.icon_02d
                "02n" -> imageInInteger = R.drawable.icon_02n
                "03n" -> imageInInteger = R.drawable.icon_03n
                "03d" -> imageInInteger = R.drawable.icon_03d
                "04d" -> imageInInteger = R.drawable.icon_04d
                "04n" -> imageInInteger = R.drawable.icon_04n
                "09d" -> imageInInteger = R.drawable.icon_09d
                "09n" -> imageInInteger = R.drawable.icon_09n
                "10d" -> imageInInteger = R.drawable.icon_10d
                "10n" -> imageInInteger = R.drawable.icon_10n
                "11d" -> imageInInteger = R.drawable.icon_11d
                "11n" -> imageInInteger = R.drawable.icon_11n
                "13d" -> imageInInteger = R.drawable.icon_13d
                "13n" -> imageInInteger = R.drawable.icon_13n
                "50d" -> imageInInteger = R.drawable.icon_50d
                "50n" -> imageInInteger = R.drawable.icon_50n
                else -> imageInInteger = R.drawable.cloud_icon
            }
            return imageInInteger
        }


        fun timeStampToHour(dt: Long): String {
            var date: Date = Date(dt * 1000)
            var dateFormat: DateFormat = SimpleDateFormat("h:mm aa")
            return dateFormat.format(date)
        }

        fun timeStampMonth(dt: Long): String {
            var date: Date = Date(dt * 1000)
            var dateFormat: DateFormat = SimpleDateFormat("dd MMM")
            return dateFormat.format(date)
        }

        fun timeStampToHourOneNumber(dt: Long): String {
            var date: Date = Date(dt * 1000)
            var dateFormat: DateFormat = SimpleDateFormat("h")
            return dateFormat.format(date)
        }

        fun checkForInternet(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false

                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(network) ?: return false

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


        fun saveLanguageToSharedPref(context: Context, key: String, value: String) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences(
                "Language",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun saveTempToSharedPref(context: Context, key: String, value: String) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences(
                "Units",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun saveLocationSharedPref(context: Context, key: String, value: String) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences(
                LOCATION_KEY,
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun saveLatitudeToSharedPref(context: Context, key: String, value: Long) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences(
                LATITUDE_KEY,
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putLong(key, value)
            editor.apply()
        }

        fun saveLongitudeToSharedPref(context: Context, key: String, value: Long) {
            var editor: SharedPreferences.Editor = context.getSharedPreferences(
                LONGITUDE_KEY,
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putLong(key, value)
            editor.apply()
        }

        fun saveNotificationToSharedPref(context: Context, key: String, value: String) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences(
                "Notification",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun convertNumbersToArabic(value: Int): String {
            return (value.toString() + "")
                .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
                .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
                .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
                .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
                .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
        }

        fun convertNumbersToArabic(value: Long): String {
            return (value.toString() + "")
                .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
                .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
                .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
                .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
                .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
        }

        fun convertNumbersToArabic(value: Double): String {
            return (value.toString() + "")
                .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
                .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
                .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
                .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
                .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
        }

        fun timeStampToDate(dt: Long): String {
            var date: Date = Date(dt * 1000)
            var dateFormat: DateFormat = SimpleDateFormat("MMM d, yyyy")
            return dateFormat.format(date)
        }

        fun dateToLong(date: String?): Long {
            val f = SimpleDateFormat("dd-MM-yyyy")
            var milliseconds: Long = 0
            try {
                val d = date?.let { f.parse(it) }
                milliseconds = d!!.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return milliseconds
        }

        fun longToDate(dt: Long): String {
            val date = Date(dt)
            val dateFormat: DateFormat = SimpleDateFormat("MMM d, yyyy")
            return dateFormat.format(date)
        }

        fun timeToMillis(hour: Int, min: Int): Long {
            return ((hour * 60 + min) * 60 * 1000).toLong()
        }

        @SuppressLint("SimpleDateFormat")
        fun convertDateToLong(date: String): Long {
            val df = SimpleDateFormat("HH : mm")
            return df.parse(date).time
        }

        fun convertDateToLong(date: String,context: Context): Long {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            val timestamp: Date = simpleDateFormat.parse(date) as Date
            return timestamp.time
        }


    }
}
