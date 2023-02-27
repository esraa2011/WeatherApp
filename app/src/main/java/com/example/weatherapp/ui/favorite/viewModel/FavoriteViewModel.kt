package com.example.weatherapp.ui.favorite.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(var repository: Repository) : ViewModel() {


    private var _favList = MutableLiveData<List<FavoriteWeatherPlacesModel>>()
    val favList: LiveData<List<FavoriteWeatherPlacesModel>> = _favList

    init {

        viewModelScope.launch(Dispatchers.IO) {
            getAllFavoritePlaces()
            _favList.postValue(repository.getFavoritePlaces())


        }


    }

    fun insertFavoriteWeather(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoritePlaces(favoriteWeatherPlacesModel)
        }
    }

    fun getAllFavoritePlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavoritePlaces()
        }
    }

    fun deleteFavoriteWeather(favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromFav(favoriteWeatherPlacesModel)
        }
    }
}