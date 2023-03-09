package com.example.weatherapp.ui.alert.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertDialogBinding
import com.example.weatherapp.models.AlarmPojo
import com.example.weatherapp.models.Utility
import com.example.weatherapp.repo.Repository
import com.example.weatherapp.ui.alert.viewModel.AlertFactoryViewModel
import com.example.weatherapp.ui.alert.viewModel.AlertsViewModel
import java.util.*


class AlertDialogFragment : DialogFragment() {
    private var _binding: FragmentAlertDialogBinding? = null
    private val binding get() = _binding!!
    lateinit var factoryViewModel: AlertFactoryViewModel


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
        factoryViewModel = AlertFactoryViewModel(Repository(requireContext()))
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



        binding.save.setOnClickListener {
            if (!binding.startDay.text.isEmpty() && !binding.endDay.text.isEmpty()
                && !binding.timeStart.text.isEmpty() && !binding.timeEnd.text.isEmpty() && !binding.zone.text.isEmpty()
            ) {
                var alert = AlarmPojo(
                    Utility.dateToLong(binding.startDay.text.toString()),
                    Utility.dateToLong(binding.endDay.text.toString()),
                    binding.timeStart.text.toString(),
                    binding.timeEnd.text.toString(),
                    binding.zone.text.toString()
                )
                viewModel.insertAlert(alert)
                NavHostFragment.findNavController(this)
                    .navigate(R.id.nav_alerts)
            } else {

//                if(binding.startDay.text.isEmpty()){
//                    Toast.makeText(requireContext(), "Enter Start Day ", Toast.LENGTH_SHORT).show()
//                }
//                 if(binding.endDay.text.isEmpty()){
//                    Toast.makeText(requireContext(), "Enter Start Day ", Toast.LENGTH_SHORT).show()
//
//                }
//                 if(binding.timeStart.text.isEmpty()){
//                    Toast.makeText(requireContext(), "Enter Start Time ", Toast.LENGTH_SHORT).show()
//
//                }
//                if(binding.timeEnd.text.isEmpty()){
//                    Toast.makeText(requireContext(), "Enter End Time ", Toast.LENGTH_SHORT).show()
//
//                }
//                if(binding.zone.text.isEmpty()){
//                    Toast.makeText(requireContext(), "Enter Zone ", Toast.LENGTH_SHORT).show()
//
//                }

                Toast.makeText(requireContext(), "Enter  ", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "Enter Data ", Toast.LENGTH_SHORT).show()
            }

        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}