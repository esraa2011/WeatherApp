package com.example.weatherapp.ui.home.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.data.repo.RepositoryOperation


class HomeFactoryViewModel(
    private val repository: RepositoryOperation
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