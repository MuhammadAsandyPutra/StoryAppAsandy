package com.example.storyappasandy.maps

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.storyappasandy.R
import com.example.storyappasandy.databinding.ActivityMapsBinding
import com.example.storyappasandy.view.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val viewModel by viewModels<MapsViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        fetchLocation()

        viewModel.locationStories.observe(this, Observer { response ->
            if (response.error == false) {
                // Data lokasi tersedia dalam response.listStory
                val locations = response.listStory

                // Menambahkan marker ke peta untuk setiap lokasi
                for (data in locations) {
                    val lat = data.lat ?: 0.0
                    val lon = data.lon ?: 0.0
                    val latLng = LatLng(lat, lon)
                    val marker = MarkerOptions()
                        .position(latLng)
                        .title(data.name)
                        .snippet(data.description)
                    mMap.addMarker(marker)

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 5f))
                }
            } else {

            }
        })



    }

    private fun fetchLocation(){
        viewModel.viewModelScope.launch {
            viewModel.fetchLocationStories()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap



        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }


}