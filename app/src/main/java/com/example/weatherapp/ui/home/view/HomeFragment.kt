package com.example.weatherapp.ui.home.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.Daily
import com.example.weatherapp.repo.Repository

import com.example.weatherapp.ui.home.viewModel.HomeViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    lateinit var daily: List<Daily>
    lateinit var hours: List<Current>
    lateinit var dailyAdapter: DailyAdapter
    lateinit var hoursAdapter: HoursAdapter
    lateinit var factoryViewModel: HomeFactoryViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        factoryViewModel = HomeFactoryViewModel(Repository(requireContext()))
        val homeViewModel =
            ViewModelProvider(this, factoryViewModel).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        homeViewModel.dailyList.observe(viewLifecycleOwner) {
            daily = it
            dailyAdapter =
                DailyAdapter(daily as List<Daily>)
            binding.homeRecycleDaily.adapter = dailyAdapter

        }
        homeViewModel.hours.observe(viewLifecycleOwner) {
            hours = it
            hoursAdapter =
                HoursAdapter(hours as List<Current>)
            binding.homeRecycleHours.adapter = hoursAdapter

        }
        homeViewModel.currentList.observe(viewLifecycleOwner) {
            binding.pressureMeasure.text = it.pressure.toString()
            binding.humidityMeasure.text = it.humidity.toString()
            binding.cloudMeasure.text = it.clouds.toString()
            binding.visibilityMeasure.text = it.visibility.toString()
            binding.windMeasure.text = it.windSpeed.toString()
            binding.violateMeasure.text = it.uvi.toString()
            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(it.weather[0].icon))
            binding.todayWeather.text =
                "${it.temp.toInt()} °C"
            binding.todayWeatherStatus.text = it.weather[0].description
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}