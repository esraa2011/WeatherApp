package com.example.weatherapp.ui.alert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.repo.RepositoryOperation
import com.example.weatherapp.ui.home.viewModel.ApiStateRoot
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertsViewModel(var repository: RepositoryOperation) : ViewModel() {


    private var _alertList = MutableStateFlow<AlertState>(AlertState.loading)
    val alertList = _alertList.asStateFlow()

    private val _stateInsetAlert = MutableStateFlow<Long>(0)
    val stateInsetAlert: StateFlow<Long>
        get() = _stateInsetAlert

    private val _stateSingleAlert = MutableStateFlow<AlarmPojo>(AlarmPojo())
    val stateSingleAlert: StateFlow<AlarmPojo>
        get() = _stateSingleAlert

    private var _root = MutableStateFlow<ApiStateRoot>(ApiStateRoot.loading)
    val root = _root.asStateFlow()

    init {
        getAllAlerts()
    }

    fun getCurrentWeather(latLng: LatLng): StateFlow<ApiStateRoot> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeatherAlert(latLng)
                .catch { e ->
                    _root.value = ApiStateRoot.Failure(e)
                }
                .collect {
                    _root.value = ApiStateRoot.Success(it)
                }
        }
        return root
    }

    fun getAllAlerts() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllAlerts()
            .catch { e ->
                _alertList.value = AlertState.Failure(e)
            }.collect {
                _alertList.value = AlertState.Success(it)
            }
    }

    fun insertAlert(alert: AlarmPojo) {
        viewModelScope.launch {

            val id = repository.insertAlert(alert)


            _stateInsetAlert.value = id
        }
    }

    fun deleteAlert(alert: AlarmPojo) {
        viewModelScope.launch {

            repository.deleteAlert(alert.id ?: -1)
        }
    }


}
