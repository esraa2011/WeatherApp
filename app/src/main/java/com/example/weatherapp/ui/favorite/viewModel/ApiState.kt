package com.example.weatherapp.ui.favorite.viewModel

import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel

sealed class ApiState {
    class Success(val data: List<FavoriteWeatherPlacesModel>) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object loading : ApiState()
}
