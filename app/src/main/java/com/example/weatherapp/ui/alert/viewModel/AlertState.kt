package com.example.weatherapp.ui.alert.viewModel

import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root

sealed class AlertState {
    class Success(val data: List<AlarmPojo>) : AlertState()
    class Failure(val msg: Throwable) : AlertState()
    object loading : AlertState()
}
