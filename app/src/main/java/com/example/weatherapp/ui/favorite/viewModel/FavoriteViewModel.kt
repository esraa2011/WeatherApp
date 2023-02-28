package com.example.weatherapp.ui.favorite.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.Daily
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.repo.Repository
import com.example.weatherapp.ui.home.viewModel.ApiStateRoot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoriteViewModel(var repository: Repository) : ViewModel() {


    private var _favList = MutableStateFlow<ApiState>(ApiState.loading)
    val favList = _favList.asStateFlow()
//
//    private var _dailyList = MutableLiveData<List<Daily>>()
//    val dailyList: LiveData<List<Daily>> = _dailyList
//
//    private var _hours = MutableLiveData<List<Current>>()
//    val hours: LiveData<List<Current>> = _hours
//
//    private var _currentList = MutableLiveData<Current>()
//    val currentList: LiveData<Current> = _currentList

private var _root =  MutableStateFlow<ApiStateRoot>(ApiStateRoot.loading)
    val root= _root.asStateFlow()

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
//            _dailyList.postValue(repository.getFavoriteWeather(favoriteWeatherPlacesModel)?.daily)
//            _hours.postValue(repository.getFavoriteWeather(favoriteWeatherPlacesModel)?.hourly)
//            _currentList.postValue(repository.getFavoriteWeather(favoriteWeatherPlacesModel)?.current)

            repository.getFavoriteWeather(favoriteWeatherPlacesModel).catch { e->
                _root.value=ApiStateRoot.Failure(e)
            }
                .collect{
                    _root.value=ApiStateRoot.Success(it)
                }
        }
    }


}