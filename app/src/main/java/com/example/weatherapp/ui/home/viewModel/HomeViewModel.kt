package com.example.weatherapp.ui.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.Utility
import com.example.weatherapp.repo.Repository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(var repository: Repository) : ViewModel() {

    private var _root = MutableStateFlow<ApiStateRoot>(ApiStateRoot.loading)
    val root = _root.asStateFlow()

    fun getWeather(latLng: LatLng) :StateFlow<ApiStateRoot>{
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeather(latLng).catch { e ->
                _root.value = ApiStateRoot.Failure(e)
            }
                ?.collect {

                    _root.value = ApiStateRoot.Success(it)
                }
        }
        return root
    }
}

