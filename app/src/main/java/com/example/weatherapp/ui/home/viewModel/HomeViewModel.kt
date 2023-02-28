package com.example.weatherapp.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.Daily
import com.example.weatherapp.models.Root
import com.example.weatherapp.repo.Repository
import com.example.weatherapp.ui.favorite.viewModel.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(repository: Repository) : ViewModel() {

    private var _root =  MutableStateFlow<ApiStateRoot>(ApiStateRoot.loading)
    val root= _root.asStateFlow()

    init {

        viewModelScope.launch(Dispatchers.IO) {
        repository.getWeather()?.catch {
            e->
            _root.value=ApiStateRoot.Failure(e)
        }
            ?.collect{
                _root.value=ApiStateRoot.Success(it)
            }
        }
    }


}

