package com.example.weatherapp.ui.alert.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.weatherapp.R
import com.example.weatherapp.data.models.AlarmPojo
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
    lateinit var alert: AlarmPojo

    companion object {
        fun newInstance() = AlertDialogFragment()
    }

    private lateinit var viewModel: AlertsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_WeatherApp_Dialog)


    }

    override fun onStart() {
        super.onStart()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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




        setInitialData()
        binding.startDay.setOnClickListener {
            showDatePicker(true)
        }

        binding.endDay.setOnClickListener {
            showDatePicker(false)
        }

        binding.save.setOnClickListener {
            if (binding.startDay.text.isEmpty() || binding.endDay.text.isEmpty() || binding.zone.text.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.Enter_Data_In_Empty_Field), Toast.LENGTH_LONG)
                    .show()
            } else {
                viewModel.insertAlert(alert)
                dialog!!.dismiss()
            }

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

        lifecycleScope.launch {
            viewModel.stateInsetAlert.collectLatest { id ->
                println(id)
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

    private fun showTimePicker(isFrom: Boolean, datePicker: Long) {
        val rightNow = Calendar.getInstance()
        val currentHour = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute = rightNow.get(Calendar.MINUTE)
        val listener: (TimePicker?, Int, Int) -> Unit =
            { _: TimePicker?, hour: Int, minute: Int ->
                val time = TimeUnit.MINUTES.toSeconds(minute.toLong()) +
                        TimeUnit.HOURS.toSeconds(hour.toLong()) - (3600L * 2)
                val dateString = dayConverterToString(datePicker, requireContext())
                val timeString = timeConverterToString(time, requireContext())
                val text = dateString.plus("\n").plus(timeString)
                if (isFrom) {
                    alert.alarmStartTime = time
                    alert.alarmStartDay = datePicker
                    binding.startDay.text = text
                } else {
                    alert.alarmEndTime = time
                    alert.alarmEndDay = datePicker
                    binding.endDay.text = text
                }
            }

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            listener, currentHour, currentMinute, false
        )

        timePickerDialog.setTitle(getString(R.string.choose))
        // timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    private fun showDatePicker(isFrom: Boolean) {
        val myCalender = Calendar.getInstance()
        val year = myCalender[Calendar.YEAR]
        val month = myCalender[Calendar.MONTH]
        val day = myCalender[Calendar.DAY_OF_MONTH]
        val myDateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                if (view.isShown) {
                    val date = "$day/${month + 1}/$year"
                    showTimePicker(isFrom, convertDateToLong(date, requireContext()))
                }
            }
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            myDateListener, year, month, day
        )
        datePickerDialog.setTitle(getString(R.string.Choose_date))
        //  datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()
    }

    private fun setInitialData() {
        val rightNow = Calendar.getInstance()

        val currentHour = TimeUnit.HOURS.toSeconds(rightNow.get(Calendar.HOUR_OF_DAY).toLong())
        val currentMinute = TimeUnit.MINUTES.toSeconds(rightNow.get(Calendar.MINUTE).toLong())
        val currentTime = (currentHour + currentMinute).minus(3600L * 2)
        val currentTimeText = timeConverterToString((currentTime + 60), requireContext())
        val afterOneHour = currentTime.plus(3600L)
        val afterOneHourText = timeConverterToString(afterOneHour, requireContext())

        val year = rightNow.get(Calendar.YEAR)
        val month = rightNow.get(Calendar.MONTH)
        val day = rightNow.get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = convertDateToLong(date, requireContext())
        val currentDate = dayConverterToString(dayNow, requireContext())

        alert =
            AlarmPojo(

                alarmStartDay = dayNow,
                alarmEndDay = dayNow,
                alarmStartTime = (currentTime + 60),
                alarmEndTime = afterOneHour,
                binding.zone.text.toString(),
                latitude = lat,
                longitude = lon
            )

        binding.startDay.text = currentDate.plus("\n").plus(currentTimeText)
        binding.endDay.text = currentDate.plus("\n").plus(afterOneHourText)
    }


}