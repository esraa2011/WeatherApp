package com.example.weatherapp.ui.settings.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.data.models.LocaleManager
import com.example.weatherapp.data.models.Utility
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    lateinit var languageShared: SharedPreferences
    lateinit var unitsShared: SharedPreferences
    lateinit var locationShared: SharedPreferences


    lateinit var lang: String
    lateinit var unit: String
    lateinit var location: String


    var flagLangEng: Boolean = false
    var flagLangArb: Boolean = false
    var flagTempKel: Boolean = false
    var flagTempFeh: Boolean = false
    var flagTempCel: Boolean = false
    var flagGps: Boolean = false


    companion object {
        fun newInstance() = SettingsFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        languageShared = requireContext().getSharedPreferences(
            Utility.Language_Value_Key, Context.MODE_PRIVATE
        )
        unitsShared = requireContext().getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
        locationShared = requireContext().getSharedPreferences(
            Utility.LOCATION_KEY, Context.MODE_PRIVATE
        )


        lang = languageShared.getString(Utility.Language_Key, "en")!!
        unit = unitsShared.getString(Utility.TEMP_KEY, "metric")!!
        location = locationShared.getString(Utility.LOCATION_KEY, "gps")!!


        settingSaveState()
        changeLanguage()
        changeTemp()


        val sharedPreferences =
            requireContext().getSharedPreferences("MySettings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val isDialogState = sharedPreferences.getBoolean("IsDialog", false)

        if (isDialogState) {
            binding.rbEnable.isChecked = false
            binding.rbDesable.isChecked = true
        } else {
            binding.rbEnable.isChecked = true
            binding.rbDesable.isChecked = false
        }


        binding.rbEnable.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editor.putBoolean("IsDialog", false)
                editor.apply()
            }
        }
        binding.rbDesable.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editor.putBoolean("IsDialog", true)
                editor.apply()

                checkPermissionOfOverlay()
            }
        }


        binding.locationGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when {
                checkedButtonId == binding.rbGps.id -> {
                    flagGps = true

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.change_to_gps_txt),
                        Toast.LENGTH_SHORT
                    ).show()

                }
                checkedButtonId == binding.rbMa.id -> {

                    Utility.saveLocationSharedPref(
                        requireContext(),
                        Utility.LOCATION_KEY,
                        Utility.MAP
                    )

                    Navigation.findNavController(root)
                        .navigate(R.id.action_nav_Settings_to_settingMapFragment)

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.change_to_map_txt),
                        Toast.LENGTH_SHORT
                    ).show()


                }
            }
        }

        binding.save.setOnClickListener {
            if (Utility.checkForInternet(requireContext())) {
                if (flagLangEng) {
                    LocaleManager.setLocale(requireContext())
                    Utility.saveLanguageToSharedPref(
                        requireContext(),
                        Utility.Language_Key,
                        Utility.Language_EN_Value
                    )
                } else if (flagLangArb) {
                    LocaleManager.setLocale(requireContext())
                    Utility.saveLanguageToSharedPref(
                        requireContext(),
                        Utility.Language_Key,
                        Utility.Language_AR_Value
                    )
                }
                if (flagTempCel) {
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.METRIC)
                } else if (flagTempKel) {
                    Utility.saveTempToSharedPref(
                        requireContext(),
                        Utility.TEMP_KEY,
                        Utility.STANDARD
                    )
                } else if (flagTempFeh) {
                    Utility.saveTempToSharedPref(
                        requireContext(),
                        Utility.TEMP_KEY,
                        Utility.IMPERIAL
                    )

                }
                if (flagGps) {
                    Utility.saveLocationSharedPref(
                        requireContext(),
                        Utility.LOCATION_KEY,
                        Utility.GPS
                    )

                }

                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_Settings_to_nav_home)

                requireActivity().recreate()
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

        return root
    }

    private fun changeLanguage() {
        binding.languageGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when {
                checkedButtonId == binding.rbEng.id -> {

                    flagLangEng = true

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.change_to_en_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                checkedButtonId == binding.rbArb.id -> {

                    flagLangArb = true
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.change_to_ar_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun changeTemp() {

        binding.tempGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when {
                checkedButtonId == binding.rbMeter.id -> {
                    flagTempCel = true

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.change_to_cel_txt),
                        Toast.LENGTH_SHORT
                    ).show()


                }
                checkedButtonId == binding.rbKelvin.id -> {
                    flagTempKel = true

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.change_to_kelvin_txt),
                        Toast.LENGTH_SHORT
                    ).show()


                }
                checkedButtonId == binding.rbFahrenheit.id -> {
                    flagTempFeh = true

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.change_to_feh_txt),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    private fun settingSaveState() {
        if (unit == Utility.IMPERIAL) {
            binding.rbFahrenheit.isChecked = true
            binding.rbKelvin.isChecked = false
            binding.rbMeter.isChecked = false

        } else if (unit == Utility.STANDARD) {
            binding.rbKelvin.isChecked = true
            binding.rbFahrenheit.isChecked = false
            binding.rbMeter.isChecked = false


        } else {
            binding.rbMeter.isChecked = true
            binding.rbKelvin.isChecked = false
            binding.rbFahrenheit.isChecked = false
        }
        if (lang == Utility.Language_EN_Value) {
            binding.rbEng.isChecked = true
            binding.rbArb.isChecked = false


        } else {
            binding.rbArb.isChecked = true
            binding.rbEng.isChecked = false
        }

        if (location == Utility.GPS) {
            binding.rbGps.isChecked = true
            binding.rbMa.isChecked = false


        } else {
            binding.rbMa.isChecked = true
            binding.rbGps.isChecked = false
        }

    }

    private fun checkPermissionOfOverlay() {
        // Check if we already  have permission
        if (!Settings.canDrawOverlays(requireContext())) {

            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("Display on top")
                .setMessage("You Should let us to draw on top")
                .setPositiveButton("Okay") { dialog: DialogInterface, _: Int ->

                    // Navigate to Manage Overlay settings in device
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().applicationContext.packageName)
                    )
                    startActivityForResult(intent, 1)
                    dialog.dismiss()

                }.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }.show()
        }
    }

}


