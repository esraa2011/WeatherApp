package com.example.weatherapp.ui.favorite.view

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
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.ui.favorite.viewModel.FavoriteFactoryViewModel
import com.example.weatherapp.ui.favorite.viewModel.FavoriteViewModel
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

class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var factoryViewModel: FavoriteFactoryViewModel
    lateinit var favoriteViewModel: FavoriteViewModel
    val PERMISSION_ID = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val repository = Repository.getRepository(requireActivity().application)
        factoryViewModel = FavoriteFactoryViewModel(repository)
        favoriteViewModel =
            ViewModelProvider(this, factoryViewModel).get(FavoriteViewModel::class.java)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return inflater.inflate(R.layout.fragment_maps, container, false)
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
            Toast.makeText(requireContext(), getString(R.string.Saved), Toast.LENGTH_LONG).show()
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
                //   val geoCoder = Geocoder(requireContext())
                //   val address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                var addressGeocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())
                try {
                    var myAddress: List<Address> =
                        addressGeocoder.getFromLocation(
                            latLng.latitude,
                            latLng.longitude,
                            2
                        ) as List<Address>
                    if (myAddress.isNotEmpty()) {
                        "${myAddress[0].subAdminArea} ${myAddress[0].adminArea}"

                        var favoriteWeatherPlacesModel = FavoriteWeatherPlacesModel(
                            " ${myAddress[0].locality}",
                            latLng.latitude,
                            latLng.longitude
                        )
                        checkSaveToFavorite(
                            favoriteWeatherPlacesModel,
                            " ${myAddress[0].locality}"
                        )
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

    fun checkSaveToFavorite(
        favoriteWeatherPlacesModel: FavoriteWeatherPlacesModel,
        placeName: String
    ) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireActivity(),R.style.MyDialogTheme)

        alert.setTitle(getString(R.string.Favorite))
        alert.setMessage("Do You want to save ${placeName} on favorite")
        alert.setIcon(R.drawable.applicatiion_icon)
        alert.setPositiveButton(getString(R.string.Save)) { _: DialogInterface, _: Int ->

            favoriteViewModel.insertFavoriteWeather(favoriteWeatherPlacesModel)
            favoriteViewModel.getAllFavoritePlaces()
            Toast.makeText(requireContext(), getString(R.string.Data), Toast.LENGTH_SHORT).show()

        }

        val dialog = alert.create()
        dialog.show()

    }


}