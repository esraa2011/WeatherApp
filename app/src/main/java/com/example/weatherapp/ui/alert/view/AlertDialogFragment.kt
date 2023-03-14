package com.example.weatherapp.ui.alert.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.weatherapp.R
import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.Utility
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.databinding.FragmentAlertDialogBinding
import com.example.weatherapp.ui.alert.viewModel.AlertFactoryViewModel
import com.example.weatherapp.ui.alert.viewModel.AlertsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit


class AlertDialogFragment : DialogFragment() {
    private var _binding: FragmentAlertDialogBinding? = null
    private val binding get() = _binding!!
    lateinit var factoryViewModel: AlertFactoryViewModel
    var lat: Double = 0.0
    var lon: Double = 0.0
    var fromTime: Long = 0
    var toTime: Long = 0

    companion object {
        fun newInstance() = AlertDialogFragment()
    }

    private lateinit var viewModel: AlertsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_WeatherApp_Dialog)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val repository = Repository.getRepository(requireActivity().application)
        factoryViewModel = AlertFactoryViewModel(repository)
        viewModel =
            ViewModelProvider(this, factoryViewModel).get(AlertsViewModel::class.java)
        _binding = FragmentAlertDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.startDay.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(

                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    binding.startDay.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                year,
                month,
                day
            )
            val dp: DatePicker = datePickerDialog.getDatePicker()

            dp.minDate = c.timeInMillis

            dp.setMinDate(System.currentTimeMillis() - 1000)
            datePickerDialog.show()
        }
        binding.endDay.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(

                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    binding.endDay.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                year,
                month,
                day
            )
            val dp: DatePicker = datePickerDialog.getDatePicker()

            dp.minDate = c.timeInMillis

            dp.setMinDate(System.currentTimeMillis() - 1000)
            datePickerDialog.show()
        }

        binding.timeStart.setOnClickListener {
            val currentTime = android.icu.util.Calendar.getInstance()
            val startHour = currentTime.get(android.icu.util.Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(android.icu.util.Calendar.MINUTE)

            TimePickerDialog(requireContext(), { view, hourOfDay, minute ->
                fromTime = (TimeUnit.MINUTES.toSeconds(minute.toLong()) + TimeUnit.HOURS.toSeconds(
                    hourOfDay.toLong()
                ))
                fromTime = fromTime.minus(3600L * 2)
                binding.timeStart.text = "$hourOfDay : $minute "

            }, startHour, startMinute, false).show()
        }
        binding.timeEnd.setOnClickListener {
            val currentTime = android.icu.util.Calendar.getInstance()
            val startHour = currentTime.get(android.icu.util.Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(android.icu.util.Calendar.MINUTE)

            TimePickerDialog(requireContext(), { view, hourOfDay, minute ->
                toTime = (TimeUnit.MINUTES.toSeconds(minute.toLong()) + TimeUnit.HOURS.toSeconds(
                    hourOfDay.toLong()
                ))
                toTime = toTime.minus(3600L * 2)
                binding.timeEnd.text = "$hourOfDay : $minute "

            }, startHour, startMinute, false).show()
        }

        binding.zone.setOnClickListener { view ->
            NavHostFragment.findNavController(this)
                .navigate(R.id.mapAlertFragment2)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("cityName")
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                binding.zone.text = result

            }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Double>("lat")
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                lat = result
            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Double>("long")
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                lon = result
            }

        binding.save.setOnClickListener {
            var alert = AlarmPojo(
                Utility.dateToLong(binding.startDay.text.toString()),
                Utility.dateToLong(binding.endDay.text.toString()),
                (fromTime + 60),
                (toTime + 60),
                binding.zone.text.toString(),
                latitude = lat,
                longitude = lon
            )
            viewModel.insertAlert(alert)
            NavHostFragment.findNavController(this)
                .navigate(R.id.nav_alerts)

        }
        lifecycleScope.launch {
            viewModel.stateInsetAlert.collectLatest { id ->
                println(id)
                // Register Worker Here and send ID of alert
                setPeriodWorkManger(id, lat, lon)
            }
        }
        return root
    }

    private fun setPeriodWorkManger(id: Long, lat: Double, long: Double) {

        val data = Data.Builder()
        data.putLong("id", id)
        data.putDouble("lat", lat)
        data.putDouble("lon", long)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            AlertPeriodicWorkManger::class.java,
            24, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "$id",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }


}