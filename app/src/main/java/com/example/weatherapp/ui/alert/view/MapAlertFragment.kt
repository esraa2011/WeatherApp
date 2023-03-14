package com.example.weatherapp.ui.alert.view

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
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
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


open class MapAlertFragment : DialogFragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PERMISSION_ID = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_WeatherApp_Dialog)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return inflater.inflate(R.layout.fragment_map_alert, container, false)
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

    override fun onMapReady(googleMap: GoogleMap) {
        // (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        getLastMLocation()
        mMap = googleMap
        mMap.setOnMapLongClickListener { latLang ->
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_LONG).show()
        }

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

            mMap.setOnMapLongClickListener { latLng ->

                mMap.addMarker(
                    MarkerOptions().position(latLng).title("Address")
                )

                val geoCoder = Geocoder(requireContext())
                val myAddress = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                checkToSave(myAddress?.get(0)?.adminArea.toString(),latLng.latitude ,latLng.longitude )

            }
        }
    }

//    fun saveToAlert(
//        placeName: String
//    ) {
//        val alert: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
//
//        alert.setTitle("Favorite")
//        alert.setMessage("Do You want to save ${placeName} on favorite")
//        alert.setPositiveButton("Save") { _: DialogInterface, _: Int ->
//
//            Toast.makeText(requireContext(), "Data has been saved", Toast.LENGTH_SHORT).show()
//            NavHostFragment.findNavController(this@MapAlertFragment).popBackStack()
//
//        }
//
//        val dialog = alert.create()
//        dialog.show()
//
//    }
fun checkToSave(placeName: String , lat: Double , long: Double) {
    val alert: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

    alert.setTitle("Alert Map")
    alert.setMessage("Do You want to save ${placeName} place on Alert")
    alert.setPositiveButton("Save") { _: DialogInterface, _: Int ->
        registerObserver(placeName,lat , long)
        NavHostFragment.findNavController(this).popBackStack()
    }
    val dialog = alert.create()
    dialog.show()

}

//    fun registerObserver(cityName: String, lat: Long, long: Long) {
//
//        val navController = findNavController();
//
//        val navBackStackEntry = navController.previousBackStackEntry
//
//        val observer = LifecycleEventObserver { _, event ->
//        }
//        navBackStackEntry?.lifecycle?.addObserver(observer)
//
//        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_DESTROY) {
//                navBackStackEntry?.savedStateHandle?.set("cityName", cityName)
//                navBackStackEntry?.savedStateHandle?.set("lat", lat)
//                navBackStackEntry?.savedStateHandle?.set("long", long)
//                navBackStackEntry?.lifecycle?.removeObserver(observer)
//            }
//        })
//    }
fun registerObserver(cityName:String,lat:Double,long: Double){

    val navController = findNavController()
    val navBackStackEntry = navController.previousBackStackEntry

    val observer = LifecycleEventObserver { _, event ->

    }
    navBackStackEntry?.lifecycle?.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            navBackStackEntry?.savedStateHandle?.set("cityName",cityName)
            navBackStackEntry?.savedStateHandle?.set("lat",lat)
            navBackStackEntry?.savedStateHandle?.set("long",long)
            navBackStackEntry?.lifecycle?.removeObserver(observer)
        }
    })
}

}