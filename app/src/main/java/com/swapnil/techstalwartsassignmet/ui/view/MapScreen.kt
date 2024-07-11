package com.swapnil.techstalwartsassignmet.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.swapnil.techstalwartsassignmet.ui.theme.TechstalwartsAssignmetTheme
import com.swapnil.techstalwartsassignmet.viewmodel.LocationViewModel

class MapScreen : ComponentActivity() {
    private val viewModel: LocationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST) {

        }
        viewModel.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        enableEdgeToEdge()
        setContent {
            var currentLocation by remember { mutableStateOf(LatLng(0.toDouble(), 0.toDouble())) }
            val cameraPosition = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    currentLocation, 20f
                )
            }
            val origin = LatLng(19.110081, 72.925857)


            var cameraPositionState by remember {
                mutableStateOf(cameraPosition)
            }
            viewModel.locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations) {
                        currentLocation = LatLng(location.latitude, location.longitude)
                        // Get the accuracy radius from the location object
                        cameraPositionState = CameraPositionState(
                            position = CameraPosition.fromLatLngZoom(
                                currentLocation, 20f
                            )
                        )
                    }
                }
            }
            val cameraPositionStateNew = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(origin, 10f)
            }
            TechstalwartsAssignmetTheme {
                LocationScreen(this@MapScreen, currentLocation, cameraPositionStateNew)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (viewModel.locationRequired) {
            viewModel.startLocationUpdates()
        }
    }
    override fun onPause() {
        super.onPause()
        viewModel.locationCallback?.let {
            viewModel.fusedLocationClient?.removeLocationUpdates(it)
        }
    }
}

