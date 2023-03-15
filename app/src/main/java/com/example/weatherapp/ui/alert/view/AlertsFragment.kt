package com.example.weatherapp.ui.alert.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.work.WorkManager
import com.example.weatherapp.R
import com.example.weatherapp.data.models.Utility
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.databinding.AlertFragmentBinding
import com.example.weatherapp.ui.alert.viewModel.AlertFactoryViewModel
import com.example.weatherapp.ui.alert.viewModel.AlertState
import com.example.weatherapp.ui.alert.viewModel.AlertsViewModel
import com.google.android.material.snackbar.Snackbar
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
            if (Utility.checkForInternet(requireContext())) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_nav_alerts_to_alertDialogFragment3)
            } else {
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





        lifecycleScope.launchWhenStarted {
            alertsViewModel.alertList.collectLatest {

                when (it) {
                    is AlertState.loading -> {

                    }
                    is AlertState.Success -> {
                        if (Utility.checkForInternet(requireContext())) {
                            adapter =
                                AlertAdapter(it.data) { it ->
                                    alertsViewModel.deleteAlert(it)
                                    WorkManager.getInstance().cancelAllWorkByTag("${it.id}")

                                }


                            binding.alertRectcleView.adapter = adapter
                            adapter.notifyDataSetChanged()
                        } else {
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
                    is AlertState.Failure -> {
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_LONG).show()
                    }

                }

            }
        }

        return root
    }

}