package com.example.weatherapp.ui.favorite.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FavoriteFragmentBinding
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.repo.Repository


import com.example.weatherapp.ui.favorite.viewModel.FavoriteViewModel
import com.example.weatherapp.ui.home.view.HoursAdapter


class FavoriteFragment : Fragment() {

    private var _binding: FavoriteFragmentBinding? = null

    private val binding get() = _binding!!
    private lateinit var factoryViewModel: FavoriteFactoryViewModel
    private lateinit var adapter: FavoriteAdapter
    lateinit var favoriteWeatherPlaces: List<FavoriteWeatherPlacesModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        factoryViewModel = FavoriteFactoryViewModel(Repository(requireContext()))
        val favoriteViewModel =
            ViewModelProvider(this, factoryViewModel).get(FavoriteViewModel::class.java)

        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.fabFav.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.Nav_map)
        }


        favoriteViewModel.getAllFavoritePlaces()
        favoriteViewModel.favList.observe(viewLifecycleOwner) {
            favoriteWeatherPlaces = it
            adapter =
                FavoriteAdapter(it){
                  favoriteWeatherPlacesModel :FavoriteWeatherPlacesModel->
                    favoriteViewModel.deleteFavoriteWeather(favoriteWeatherPlacesModel)
                }
            favoriteViewModel.getAllFavoritePlaces()
            binding.FavoriteRectcleView.adapter = adapter

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}