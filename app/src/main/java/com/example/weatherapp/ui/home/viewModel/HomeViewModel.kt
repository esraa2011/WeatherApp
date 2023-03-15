package com.example.weatherapp.ui.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repo.RepositoryOperation
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(var repository: RepositoryOperation) : ViewModel() {

    private var _root = MutableStateFlow<ApiStateRoot>(ApiStateRoot.loading)
    val root = _root.asStateFlow()

    fun getWeather(latLng: LatLng): StateFlow<ApiStateRoot> {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                repository.getWeather(latLng)
                    .catch { e ->
                        _root.value = ApiStateRoot.Failure(e)
                    }
                    .collect {
                        if (it != null) {
                            _root.value = ApiStateRoot.Success(it)
                        } else {
                            _root.value = ApiStateRoot.Failure(it)
                        }

                    }
            } catch (e: IOException) {
                _root.value = ApiStateRoot.Failure(e)

            }
        }
        return root
    }
}

