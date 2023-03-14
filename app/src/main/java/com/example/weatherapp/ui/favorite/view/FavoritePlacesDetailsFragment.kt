package com.example.weatherapp.ui.favorite.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Utility
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.databinding.FragmentFavoritePlacesDetailsBinding
import com.example.weatherapp.ui.favorite.viewModel.FavoriteFactoryViewModel
import com.example.weatherapp.ui.favorite.viewModel.FavoriteViewModel
import com.example.weatherapp.ui.home.view.DailyAdapter
import com.example.weatherapp.ui.home.view.HoursAdapter
import com.example.weatherapp.ui.home.viewModel.ApiStateRoot
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoritePlacesDetailsFragment : Fragment() {
    lateinit var factoryViewModel: FavoriteFactoryViewModel
    private val binding get() = _binding!!
    lateinit var dailyAdapter: DailyAdapter
    lateinit var hoursAdapter: HoursAdapter
    private var _binding: FragmentFavoritePlacesDetailsBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var languageShared: SharedPreferences
    lateinit var unitsShared: SharedPreferences
    lateinit var lang: String
    lateinit var unit: String
    lateinit var favorite: FavoriteWeatherPlacesModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val repository = Repository.getRepository(requireActivity().application)
        factoryViewModel = FavoriteFactoryViewModel(repository)
        val favoriteViewModel =
            ViewModelProvider(this, factoryViewModel).get(FavoriteViewModel::class.java)
        _binding = FragmentFavoritePlacesDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        arguments?.let {
            favorite = it.getSerializable("favorite") as FavoriteWeatherPlacesModel
            favoriteViewModel.getAllFavoritePlacesDetails(favorite as FavoriteWeatherPlacesModel)
            languageShared = requireContext().getSharedPreferences("Language", Context.MODE_PRIVATE)
            unitsShared =
                requireContext().getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
            lang = languageShared.getString(Utility.Language_Key, "en")!!
            unit = unitsShared.getString(Utility.TEMP_KEY, "metric")!!
        }
        favoriteViewModel.getAllFavoritePlacesDetails(favorite)
        lifecycleScope.launch {
            favoriteViewModel.root.collectLatest { result ->
                when (result) {

                    is ApiStateRoot.loading -> {
                        binding.gifHome.visibility = View.VISIBLE
                        binding.cardView.visibility = View.GONE
                        binding.cardViewWind.visibility = View.GONE
                        binding.cardViewClouds.visibility = View.GONE
                        binding.cardViewHumidity.visibility = View.GONE
                        binding.cardViewUvi.visibility = View.GONE
                        binding.cardViewPressure.visibility = View.GONE
                        binding.cardViewVisibility.visibility = View.GONE

                    }
                    is ApiStateRoot.Success -> {
                        binding.gifHome.visibility = View.GONE
                        binding.cardView.visibility = View.VISIBLE
                        binding.cardViewWind.visibility = View.VISIBLE
                        binding.cardViewClouds.visibility = View.VISIBLE
                        binding.cardViewHumidity.visibility = View.VISIBLE
                        binding.cardViewUvi.visibility = View.VISIBLE
                        binding.cardViewPressure.visibility = View.VISIBLE
                        binding.cardViewVisibility.visibility = View.VISIBLE

                        dailyAdapter =
                            DailyAdapter(result.data.daily, requireContext())
                        binding.homeRecycleDaily.adapter = dailyAdapter

                        hoursAdapter =
                            result.data.hourly?.let { HoursAdapter(it, requireContext()) }!!
                        binding.homeRecycleHours.adapter = hoursAdapter

                        if (lang == "en" && unit == "metric") {
                            binding.time.text =
                                "${result.data.current?.let { Utility.timeStampMonth(it.dt) }},${
                                    result.data.current?.let {
                                        Utility.timeStampToHour(
                                            it.dt
                                        )
                                    }
                                }"
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text =
                                "${result.data.current?.temp!!.toInt()} ℃"
                            binding.pressureMeasure.text = "${result.data.current?.pressure} hPa"
                            binding.humidityMeasure.text =
                                "${result.data.current!!.humidity} %"
                            binding.windMeasure.text =
                                "${result.data.current!!.windSpeed} m/s"
                            binding.cloudMeasure.text =
                                "${result.data.current!!.clouds} m"
                            binding.violateMeasure.text =
                                "${result.data.current!!.uvi} %"
                            binding.visibilityMeasure.text =
                                "${result.data.current!!.visibility} %"

                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current!!.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current?.weather!![0].description

                        } else if (lang == "ar" && unit == "metric") {
                            binding.time.text =
                                "${result.data.current?.let { Utility.timeStampMonth(it.dt) }},${
                                    result.data.current?.let {
                                        Utility.timeStampToHour(
                                            it.dt
                                        )
                                    }
                                }"
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text =
                                Utility.convertNumbersToArabic(result.data.current?.temp!!.toInt()) + " س°"

                            binding.pressureMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.pressure) + "هـ ب أ"
                            binding.humidityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current!!.humidity)} %"
                            binding.windMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.windSpeed) + " م/ث "
                            binding.cloudMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.clouds) + " م "
                            binding.violateMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.uvi) + " %"
                            binding.visibilityMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.visibility) + " %"

                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current!!.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current!!.weather[0].description

                        } else if (lang == "ar" && unit == "imperial") {
                            binding.time.text =
                                "${result.data.current?.let { Utility.timeStampMonth(it.dt) }},${
                                    result.data.current?.let {
                                        Utility.timeStampToHour(
                                            it.dt
                                        )
                                    }
                                }"
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text =
                                Utility.convertNumbersToArabic(result.data.current!!.temp.toInt()) + "ف° "
                            binding.pressureMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.pressure) + "هـ ب أ"
                            binding.humidityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current!!!!.humidity)} %"
                            binding.windMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.windSpeed) + " كم/س "
                            binding.cloudMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.clouds) + " كم "
                            binding.violateMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.uvi) + " %"
                            binding.visibilityMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.visibility) + " %"
                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current!!.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current!!.weather[0].description

                        } else if (lang == "en" && unit == "imperial") {
                            binding.time.text =
                                "${result.data.current?.let { Utility.timeStampMonth(it.dt) }},${
                                    result.data.current?.let {
                                        Utility.timeStampToHour(
                                            it.dt
                                        )
                                    }
                                }"
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text = "${result.data.current!!.temp.toInt()} ℉"
                            binding.pressureMeasure.text = "${result.data.current!!.pressure} hPa"
                            binding.humidityMeasure.text =
                                "${result.data.current!!.humidity} %"
                            binding.windMeasure.text =
                                "${result.data.current!!.windSpeed}  km/h"
                            binding.cloudMeasure.text =
                                "${result.data.current!!.clouds} Km"
                            binding.violateMeasure.text =
                                "${result.data.current!!.uvi} %"
                            binding.visibilityMeasure.text =
                                "${result.data.current!!.visibility} %"

                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current!!.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current!!.weather[0].description

                        } else if (lang == "en" && unit == "standard") {
                            binding.time.text =
                                "${result.data.current?.let { Utility.timeStampMonth(it.dt) }},${
                                    result.data.current?.let {
                                        Utility.timeStampToHour(
                                            it.dt
                                        )
                                    }
                                }"
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text = "${result.data.current!!.temp.toInt()} °K"
                            binding.pressureMeasure.text = "${result.data.current!!.pressure} hPa"
                            binding.humidityMeasure.text =
                                "${result.data.current!!.humidity} %"
                            binding.windMeasure.text =
                                "${result.data.current!!.windSpeed}  m/s"
                            binding.cloudMeasure.text =
                                "${result.data.current!!.clouds} m"
                            binding.violateMeasure.text =
                                "${result.data.current!!.uvi} %"
                            binding.visibilityMeasure.text =
                                "${result.data.current!!.visibility} %"


                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current!!.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current?.weather!![0].description

                        } else if (lang == "ar" && unit == "standard") {
                            binding.time.text =
                                "${result.data.current?.let { Utility.timeStampMonth(it.dt) }},${
                                    result.data.current?.let {
                                        Utility.timeStampToHour(
                                            it.dt
                                        )
                                    }
                                }"
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text =
                                Utility.convertNumbersToArabic(result.data.current!!.temp.toInt()) + "ك°"

                            binding.pressureMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.pressure) + "هـ ب أ"
                            binding.humidityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current!!.humidity)} %"
                            binding.windMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.windSpeed) + " م/ث "
                            binding.cloudMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.clouds) + " م "
                            binding.violateMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.uvi) + " %"
                            binding.visibilityMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current!!.visibility) + " %"

                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current!!.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current!!.weather[0].description
                        }

                    }
                    is ApiStateRoot.Failure -> {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.no_internet_txt),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Setting", View.OnClickListener {
                                startActivityForResult(
                                    Intent(
                                        Settings.ACTION_SETTINGS
                                    ), 0
                                );
                            }).show()
                    }
                }
            }
        }
        return root
    }
}


