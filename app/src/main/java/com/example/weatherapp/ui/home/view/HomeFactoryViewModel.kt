package com.example.weatherapp.ui.home.view


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repo.Repository
import com.example.weatherapp.ui.home.viewModel.HomeViewModel


class HomeFactoryViewModel(
    private val repository: Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repository) as T
        } else {
            throw IllegalAccessException("ViewModel Class Not Founded")
        }
    }
}