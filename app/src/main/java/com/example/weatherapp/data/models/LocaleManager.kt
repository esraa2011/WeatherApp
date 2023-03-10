package com.example.weatherapp.data.models

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LocaleManager {

    companion object {
        fun setLocale(context: Context) {
            setNewLocale(context, getLanguage(context))
            update(context, getLanguage(context))
        }

        fun setNewLocale(context: Context, language: String) {
            persistLanguage(context, language)
            update(context, language)
        }

        fun update(context: Context, language: String) {
            updateResources(context, language)
            var appContext: Context = context.applicationContext
            if (context != appContext) {
                updateResources(appContext, language)
            }
        }

        fun getLanguage(context: Context): String {
            var languageShared: SharedPreferences =
                context.getSharedPreferences("Language", AppCompatActivity.MODE_PRIVATE)
            return languageShared.getString(Utility.Language_Key, "en") ?: ""
        }

        fun persistLanguage(context: Context, language: String) {
            Utility.saveLanguageToSharedPref(context, Utility.Language_Key, language)
        }

        @SuppressLint("ObsoleteSdkInt")
        fun updateResources(context: Context, language: String) {
            var locale: Locale = Locale(language)
            Locale.setDefault(locale)

            var resources: Resources = context.resources
            var config: Configuration = Configuration(resources.configuration)
            if (Build.VERSION.SDK_INT >= 17) {
                config.setLocale(locale)
            } else {
                config.locale = locale
            }

            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }
}