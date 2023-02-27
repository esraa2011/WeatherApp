package com.example.weatherapp.ui.favorite.view


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repo.Repository
import com.example.weatherapp.ui.favorite.viewModel.FavoriteViewModel



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