package com.example.weatherapp.ui.home.viewModel

import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root

sealed class ApiStateRoot {
    class Success(val data: Root) : ApiStateRoot()
    class Failure(val msg: Throwable) : ApiStateRoot()
    object loading : ApiStateRoot()
}
