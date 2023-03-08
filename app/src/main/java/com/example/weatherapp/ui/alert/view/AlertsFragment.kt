package com.example.weatherapp.ui.alert.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.databinding.AlertFragmentBinding
import com.example.weatherapp.ui.alert.viewModel.AlertsViewModel


class AlertsFragment : Fragment() {

    private var _binding: AlertFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val alertsViewModel =
            ViewModelProvider(this).get(AlertsViewModel::class.java)

        _binding = AlertFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabAlert.setOnClickListener { view ->
            Navigation.findNavController(view)
                .navigate(R.id.action_nav_alerts_to_alertDialogFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}