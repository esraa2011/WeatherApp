package com.example.weatherapp.ui.alert.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repo.Repository


class AlertFactoryViewModel(
    private val repository: Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertsViewModel::class.java)) {
            AlertsViewModel (repository) as T
        } else {
            throw IllegalAccessException("ViewModel Class Not Founded")
        }
    }
}