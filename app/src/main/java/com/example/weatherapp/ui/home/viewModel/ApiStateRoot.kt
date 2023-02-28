package com.example.weatherapp.ui.home.viewModel

import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.models.Root

sealed class ApiStateRoot {
    class Success(val data: Root) : ApiStateRoot()
    class Failure(val msg: Throwable) : ApiStateRoot()
    object loading : ApiStateRoot()
}
