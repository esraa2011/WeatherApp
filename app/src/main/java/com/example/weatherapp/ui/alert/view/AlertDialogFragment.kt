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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertDialogBinding
import com.example.weatherapp.ui.alert.viewModel.AlertDialogViewModel
import java.util.*


class AlertDialogFragment : DialogFragment() {
    private var _binding: FragmentAlertDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = AlertDialogFragment()
    }

    private lateinit var viewModel: AlertDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_WeatherApp_Dialog)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

            val currentTime = Calendar.getInstance()
            val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { view, hourOfDay, minute ->
                binding.timeStart.text = "$hourOfDay : $minute "
            }, startHour, startMinute, true).show()
        }

        binding.timeEnd.setOnClickListener {

            val currentTime = Calendar.getInstance()
            val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { view, hourOfDay, minute ->
                binding.timeEnd.text = "$hourOfDay : $minute "
            }, startHour, startMinute, true).show()
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
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlertDialogViewModel::class.java)

    }

}