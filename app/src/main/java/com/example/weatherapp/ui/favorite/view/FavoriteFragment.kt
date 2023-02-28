package com.example.weatherapp.ui.favorite.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FavoriteFragmentBinding
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.repo.Repository
import com.example.weatherapp.ui.favorite.viewModel.ApiState


import com.example.weatherapp.ui.favorite.viewModel.FavoriteViewModel
import com.example.weatherapp.ui.home.view.HomeFactoryViewModel
import com.example.weatherapp.ui.home.view.HoursAdapter
import com.example.weatherapp.ui.home.viewModel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest


class FavoriteFragment : Fragment() {

    private var _binding: FavoriteFragmentBinding? = null

    private val binding get() = _binding!!
    lateinit var factoryViewModel: FavoriteFactoryViewModel


    private lateinit var adapter: FavoriteAdapter


    @SuppressLint("NotifyDataSetChanged")
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


        lifecycleScope.launchWhenStarted {
            favoriteViewModel.favList.collectLatest {

                when (it) {

                    is ApiState.loading -> {

                    }
                    is ApiState.Success -> {

                        adapter =
                            FavoriteAdapter(it.data, { it ->
                                favoriteViewModel.deleteFavoriteWeather(it)

                            })
                            { it ->
                                favoriteViewModel.getAllFavoritePlacesDetails(it)
                                val bundle = Bundle()
                                bundle.putSerializable("favorite", it)
                                Navigation.findNavController(root!!)
                                    .navigate(
                                        R.id.action_nav_favorite_to_favoritePlacesDetailsFragment,
                                        bundle
                                    )

                            }

                        binding.FavoriteRectcleView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_LONG).show()
                    }

                }


            }
        }

        return root
    }
}