package com.example.weatherapp.ui.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavoritePlacesDetailsBinding
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.Daily
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.repo.Repository
import com.example.weatherapp.ui.favorite.viewModel.FavoriteViewModel
import com.example.weatherapp.ui.home.view.DailyAdapter
import com.example.weatherapp.ui.home.view.HoursAdapter
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherapp.ui.home.viewModel.ApiStateRoot
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoritePlacesDetailsFragment : Fragment() {
    lateinit var factoryViewModel: FavoriteFactoryViewModel
    private val binding get() = _binding!!
    lateinit var daily: List<Daily>
    lateinit var hours: List<Current>
    lateinit var dailyAdapter: DailyAdapter
    lateinit var hoursAdapter: HoursAdapter
    private var _binding: FragmentFavoritePlacesDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        factoryViewModel = FavoriteFactoryViewModel(Repository(requireContext()))
        val favoriteViewModel =
            ViewModelProvider(this, factoryViewModel).get(FavoriteViewModel::class.java)
        _binding = FragmentFavoritePlacesDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        arguments?.let {
            var favorite = it.getSerializable("favorite")
            println("Place ${favorite}")
            favoriteViewModel.getAllFavoritePlacesDetails(favorite as FavoriteWeatherPlacesModel)
        }

//        favoriteViewModel.dailyList.observe(viewLifecycleOwner) {
//            daily = it
//            dailyAdapter =
//                DailyAdapter(daily as List<Daily>)
//            binding.homeRecycleDaily.adapter = dailyAdapter
//
//        }
//        favoriteViewModel.hours.observe(viewLifecycleOwner) {
//            hours = it
//            hoursAdapter =
//                HoursAdapter(hours as List<Current>)
//            binding.homeRecycleHours.adapter = hoursAdapter
//
//        }
//        favoriteViewModel.currentList.observe(viewLifecycleOwner) {
//            binding.pressureMeasure.text = it.pressure.toString()
//            binding.humidityMeasure.text = it.humidity.toString()
//            binding.cloudMeasure.text = it.clouds.toString()
//            binding.visibilityMeasure.text = it.visibility.toString()
//            binding.windMeasure.text = it.windSpeed.toString()
//            binding.violateMeasure.text = it.uvi.toString()
//            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(it.weather[0].icon))
//            binding.todayWeather.text =
//                "${it.temp.toInt()} °C"
//            binding.todayWeatherStatus.text = it.weather[0].description
//        }

        lifecycleScope.launch {
            favoriteViewModel.root.collectLatest { result ->
                when (result) {
                    is ApiStateRoot.Success -> {
                        dailyAdapter =
                            DailyAdapter(result.data.daily)
                        binding.homeRecycleDaily.adapter = dailyAdapter
                        binding.pressureMeasure.text = result.data.current.pressure.toString()
                        binding.humidityMeasure.text = result.data.current.humidity.toString()
                        binding.cloudMeasure.text = result.data.current.clouds.toString()
                        binding.visibilityMeasure.text = result.data.current.visibility.toString()
                        binding.windMeasure.text = result.data.current.windSpeed.toString()
                        binding.violateMeasure.text = result.data.current.uvi.toString()
                        binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current.weather[0].icon))
                        binding.todayWeather.text =
                            "${result.data.current.temp.toInt()} °C"
                        binding.todayWeatherStatus.text = result.data.current.weather[0].description

                        hoursAdapter =
                            HoursAdapter(result.data.hourly)
                        binding.homeRecycleHours.adapter = hoursAdapter
                    }
                    is ApiStateRoot.Failure -> {

                    }
                    else -> {

                    }
                }
            }
        }
        return root
    }
}


