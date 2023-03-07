package com.example.weatherapp.ui.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.models.Utility
import com.example.weatherapp.repo.Repository
import com.example.weatherapp.ui.home.viewModel.ApiStateRoot
import com.example.weatherapp.ui.home.viewModel.HomeFactoryViewModel
import com.example.weatherapp.ui.home.viewModel.HomeViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var dailyAdapter: DailyAdapter
    lateinit var hoursAdapter: HoursAdapter
    lateinit var factoryViewModel: HomeFactoryViewModel
    lateinit var languageShared: SharedPreferences
    lateinit var unitsShared: SharedPreferences
    lateinit var lang: String
    lateinit var unit: String
    val PERMISSION_ID = 10
    lateinit var homeViewModel: HomeViewModel
    // var latLng = LatLng( 0.0,  0.0)


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        factoryViewModel = HomeFactoryViewModel(Repository(requireContext()))

        homeViewModel =
            ViewModelProvider(this, factoryViewModel).get(HomeViewModel::class.java)


        languageShared = requireContext().getSharedPreferences("Language", Context.MODE_PRIVATE)

        unitsShared = requireContext().getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)

        lang = languageShared.getString(Utility.Language_Key, "en")!!

        unit = unitsShared.getString(Utility.TEMP_KEY, "metric")!!


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root




        lifecycleScope.launch {

            getLastMLocation()
            homeViewModel.root.collectLatest { result ->
                when (result) {

                    is ApiStateRoot.loading -> {
                        binding.gifHome.visibility = View.VISIBLE
                        binding.cardView.visibility = View.GONE
                        binding.cardHome.visibility = View.GONE
                    }
                    is ApiStateRoot.Success -> {

                        //  getLastMLocation()
                        binding.gifHome.visibility = View.GONE
                        binding.cardView.visibility = View.VISIBLE
                        binding.cardHome.visibility = View.VISIBLE

                        if (lang == "en" && unit == "metric") {
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text =
                                "${result.data.current.temp.toInt()} ℃"
                            binding.pressureMeasure.text = "${result.data.current.pressure} hPa"
                            binding.humidityMeasure.text =
                                "${result.data.current.humidity} %"
                            binding.windMeasure.text =
                                "${result.data.current.windSpeed} m/s"
                            binding.cloudMeasure.text =
                                "${result.data.current.clouds} m"
                            binding.violateMeasure.text =
                                "${result.data.current.uvi} %"
                            binding.visibilityMeasure.text =
                                "${result.data.current.visibility} %"

                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current.weather[0].description

                        } else if (lang == "ar" && unit == "metric") {
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text =
                                Utility.convertNumbersToArabic(result.data.current.temp.toInt()) + " س°"

                            binding.pressureMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.pressure) + "هـ ب أ"
                            binding.humidityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.humidity)} %"
                            binding.windMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.windSpeed) + " م/ث "
                            binding.cloudMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.clouds) + " م "
                            binding.violateMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.uvi) + " %"
                            binding.visibilityMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.visibility) + " %"

                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current.weather[0].description

                        } else if (lang == "ar" && unit == "imperial") {
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text =
                                Utility.convertNumbersToArabic(result.data.current.temp.toInt()) + "ف° "
                            binding.pressureMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.pressure) + "هـ ب أ"
                            binding.humidityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.humidity)} %"
                            binding.windMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.windSpeed) + " كم/س "
                            binding.cloudMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.clouds) + " كم "
                            binding.violateMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.uvi) + " %"
                            binding.visibilityMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.visibility) + " %"
                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current.weather[0].description

                        } else if (lang == "en" && unit == "imperial") {
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text = "${result.data.current.temp.toInt()} ℉"
                            binding.pressureMeasure.text = "${result.data.current.pressure} hPa"
                            binding.humidityMeasure.text =
                                "${result.data.current.humidity} %"
                            binding.windMeasure.text =
                                "${result.data.current.windSpeed}  km/h"
                            binding.cloudMeasure.text =
                                "${result.data.current.clouds} Km"
                            binding.violateMeasure.text =
                                "${result.data.current.uvi} %"
                            binding.visibilityMeasure.text =
                                "${result.data.current.visibility} %"

                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current.weather[0].description

                        } else if (lang == "en" && unit == "standard") {
                            binding.area.text = result.data.timezone
                            binding.todayWeather.text = "${result.data.current.temp.toInt()}  °K"
                            binding.pressureMeasure.text = "${result.data.current.pressure} hPa"
                            binding.humidityMeasure.text =
                                "${result.data.current.humidity} %"
                            binding.windMeasure.text =
                                "${result.data.current.windSpeed}  m/s"
                            binding.cloudMeasure.text =
                                "${result.data.current.clouds} m"
                            binding.violateMeasure.text =
                                "${result.data.current.uvi} %"
                            binding.visibilityMeasure.text =
                                "${result.data.current.visibility} %"


                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current.weather[0].description

                        } else if (lang == "ar" && unit == "standard") {
                            binding.todayWeather.text =
                                Utility.convertNumbersToArabic(result.data.current.temp.toInt()) + "ك°"

                            binding.pressureMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.pressure) + "هـ ب أ"
                            binding.humidityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.humidity)} %"
                            binding.windMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.windSpeed) + " م/ث "
                            binding.cloudMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.clouds) + " م "
                            binding.violateMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.uvi) + " %"
                            binding.visibilityMeasure.text =
                                Utility.convertNumbersToArabic(result.data.current.visibility) + " %"

                            binding.todayWeatherImg.setImageResource(Utility.getWeatherIcon(result.data.current.weather[0].icon))
                            binding.todayWeatherStatus.text =
                                result.data.current.weather[0].description
                        }
                        dailyAdapter =
                            DailyAdapter(result.data.daily, requireContext())
                        binding.homeRecycleDaily.adapter = dailyAdapter

                        hoursAdapter =
                            HoursAdapter(result.data.hourly, requireContext())
                        binding.homeRecycleHours.adapter = hoursAdapter


                    }
                    is ApiStateRoot.Failure -> {
                        // homeViewModel.g

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

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf<String>(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // getLastMLocation()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getLastMLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(200000)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )

    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            var mLastLocation: Location? = locationResult.getLastLocation()
            var latLng = LatLng(mLastLocation?.latitude ?: 0.0, mLastLocation?.longitude ?: 0.0)
            homeViewModel.getWeather(latLng)
            Log.i("mloc", "onLocationResult: first")
        }
    }


}