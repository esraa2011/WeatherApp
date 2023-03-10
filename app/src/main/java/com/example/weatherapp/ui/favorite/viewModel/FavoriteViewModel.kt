package com.example.weatherapp.ui.favorite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.ui.home.viewModel.ApiStateRoot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(var repository: Repository) : ViewModel() {


    private var _favList = MutableStateFlow<ApiState>(ApiState.loading)
    val favList = _favList.asStateFlow()

    private var _root = MutableStateFlow<ApiStateRoot>(ApiStateRoot.loading)
    val root = _root.asStateFlow()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            getAllFavoritePlaces()

        }


    }

    fun insertFavoriteWeather(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoritePlaces(favoriteWeatherPlacesModel)
        }
    }

    fun getAllFavoritePlaces() = viewModelScope.launch {

        repository.getFavoritePlaces().catch {
            _favList.value = ApiState.Failure(it)
        }.collect {
            _favList.value = ApiState.Success(it)
        }
    }

    fun deleteFavoriteWeather(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromFav(favoriteWeatherPlacesModel)
        }
    }

    fun getAllFavoritePlacesDetails(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        viewModelScope.launch(Dispatchers.IO) {

            repository.getFavoriteWeather(favoriteWeatherPlacesModel).catch { e ->
                _root.value = ApiStateRoot.Failure(e)
            }
                .collect {
                    _root.value = ApiStateRoot.Success(it)
                }
        }
    }


}