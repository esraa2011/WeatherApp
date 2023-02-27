package com.example.weatherapp.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.Daily
import com.example.weatherapp.models.Root
import com.example.weatherapp.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(repository: Repository) : ViewModel() {


    private var _dailyList = MutableLiveData<List<Daily>>()
    val dailyList: LiveData<List<Daily>> = _dailyList

    private var _hours = MutableLiveData<List<Current>>()
    val hours: LiveData<List<Current>> = _hours

    private var _currentList = MutableLiveData<Current>()
    val currentList: LiveData<Current> = _currentList


    init {

        viewModelScope.launch(Dispatchers.IO) {
            _dailyList.postValue(repository.getWeather()?.daily)
            _hours.postValue(repository.getWeather()?.hourly)
            _currentList.postValue(repository.getWeather()?.current)

        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = _dailyList.value?.get(0)?.temp.toString()
    }
    val text: LiveData<String> = _text
}

