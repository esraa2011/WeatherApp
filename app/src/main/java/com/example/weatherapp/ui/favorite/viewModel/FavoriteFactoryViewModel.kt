package com.example.weatherapp.ui.favorite.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repo.Repository


class FavoriteFactoryViewModel(
    private val repository: Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            FavoriteViewModel (repository) as T
        } else {
            throw IllegalAccessException("ViewModel Class Not Founded")
        }
    }
}