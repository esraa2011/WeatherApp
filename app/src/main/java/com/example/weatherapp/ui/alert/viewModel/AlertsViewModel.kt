package com.example.weatherapp.ui.alert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.AlarmPojo
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.repo.Repository
import com.example.weatherapp.ui.favorite.viewModel.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertsViewModel(var repository: Repository) : ViewModel() {

    private var _alertList = MutableStateFlow<AlertState>(AlertState.loading)
    val alertList = _alertList.asStateFlow()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            getAllAlerts()

        }


    }


    fun insertAlert(alert: AlarmPojo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlert(alert)
        }
    }

    fun getAllAlerts() = viewModelScope.launch {

        repository.getAlert().catch {
            _alertList.value = AlertState.Failure(it)
        }.collect {
            _alertList.value = AlertState.Success(it)
        }
    }

    fun deleteAlert(alert: AlarmPojo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlert(alert)
        }
    }

}