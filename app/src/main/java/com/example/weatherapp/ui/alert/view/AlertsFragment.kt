package com.example.weatherapp.ui.alert.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.databinding.AlertFragmentBinding
import com.example.weatherapp.ui.alert.viewModel.AlertFactoryViewModel
import com.example.weatherapp.ui.alert.viewModel.AlertState
import com.example.weatherapp.ui.alert.viewModel.AlertsViewModel
import kotlinx.coroutines.flow.collectLatest


class AlertsFragment : Fragment() {

    private var _binding: AlertFragmentBinding? = null

    private val binding get() = _binding!!
    lateinit var factoryViewModel: AlertFactoryViewModel


    private lateinit var adapter: AlertAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val repository = Repository.getRepository(requireActivity().application)
        factoryViewModel = AlertFactoryViewModel(repository)


        val alertsViewModel =
            ViewModelProvider(this, factoryViewModel).get(AlertsViewModel::class.java)

        _binding = AlertFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabAlert.setOnClickListener { view ->
            Navigation.findNavController(view)
                .navigate(R.id.action_nav_alerts_to_alertDialogFragment)
        }
        lifecycleScope.launchWhenStarted {
            alertsViewModel.alertList.collectLatest {

                when (it) {
                    is AlertState.loading -> {

                    }
                    is AlertState.Success -> {

                        adapter =
                            AlertAdapter(it.data) { it ->
                                alertsViewModel.deleteAlert(it)

                            }


                        binding.alertRectcleView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                    is AlertState.Failure -> {
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_LONG).show()
                    }

                }

            }
        }

        return root
    }

}