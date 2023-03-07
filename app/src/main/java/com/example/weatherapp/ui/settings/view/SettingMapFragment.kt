package com.example.weatherapp.ui.settings.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.models.Utility
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*

class SettingMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    val PERMISSION_ID = 10


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return inflater.inflate(R.layout.fragment_setting_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        val apiKey = getString(R.string.api_key)
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES)
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.PHOTO_METADATAS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.let {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(it)
                            .title(place.name)
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))

                }
            }

            override fun onError(status: Status) {
                Toast.makeText(requireContext(), status.toString(), Toast.LENGTH_SHORT).show();

            }
        })
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
        Log.i("per", "requestPermission: ")

        requestPermissions(
            arrayOf<String>(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation()
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

    override fun onMapReady(googleMap: GoogleMap) {
        getLastMLocation()
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.zoomTo(5f))


    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult != null) {
                super.onLocationResult(locationResult)
            }

            val mLastLocation: Location? = locationResult.getLastLocation()
            val latLng = LatLng(mLastLocation?.latitude ?: 0.0, mLastLocation?.longitude ?: 0.0)

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("My Location")
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))

//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//            mMap.moveCamera(CameraUpdateFactory.zoomTo(5f))

            mMap.setOnMapLongClickListener { latLng ->

                mMap.addMarker(
                    MarkerOptions().position(latLng).title("Address")
                )
//                val geoCoder = Geocoder(requireContext())
//                val address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                var addressGeocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())
                try {
                    var myAddress: List<Address> =
                        addressGeocoder.getFromLocation(
                            latLng.latitude,
                            latLng.longitude,
                            2
                        ) as List<Address>
                    if (myAddress.isNotEmpty()) {

                        confirmThisLocation(latLng, "${myAddress[0].adminArea}")


                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Snackbar.make(
                        requireView(),
                        getString(R.string.map_try),
                        Snackbar.LENGTH_LONG
                    )
                }


            }
        }
    }

    fun confirmThisLocation(latLng: LatLng, placeName: String) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

        alert.setTitle("Current Location")

        alert.setMessage("Do You want to Make $placeName place your current Location")
        alert.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            Utility.saveLatitudeToSharedPref(
                requireContext(),
                Utility.LATITUDE_KEY,
                latLng.latitude.toLong()
            )
            Utility.saveLongitudeToSharedPref(
                requireContext(),
                Utility.LONGITUDE_KEY,
                latLng.longitude.toLong()
            )
//            Navigation.findNavController(requireView())
//                .navigate(R.id.action_settingMapFragment_to_nav_home)

        }
        alert.setNegativeButton("No") { _: DialogInterface, _: Int ->

        }

        val dialog = alert.create()
        dialog.show()

    }


}