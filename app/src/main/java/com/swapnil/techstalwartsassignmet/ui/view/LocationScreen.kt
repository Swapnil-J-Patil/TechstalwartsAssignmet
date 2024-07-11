package com.swapnil.techstalwartsassignmet.ui.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.maps.android.compose.rememberMarkerState
import com.swapnil.techstalwartsassignmet.R
import com.swapnil.techstalwartsassignmet.viewmodel.LocationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LocationScreen(
    context: Context,
    currentLocation: LatLng,
    cameraPositionState: CameraPositionState,
) {
    val viewModel: LocationViewModel = viewModel()

    val coroutineScope = rememberCoroutineScope()
    var orientationAngle by remember { mutableStateOf(0f) } // Initial orientation angle
    var azimuthDegrees by remember { mutableStateOf(0f) }
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometerSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    val magnetometerSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) }

    val originalBitmap: Bitmap = remember { BitmapFactory.decodeResource(context.resources, R.drawable.navigation) }
    val desiredWidth = 150 // Specify the desired width in pixels
    val desiredHeight = 150 // Specify the desired height in pixels
    val resizedBitmap: Bitmap = remember { Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true) }
    var rotatedBitmap: Bitmap? by remember { mutableStateOf(null) }
    var bitmapDescriptor: BitmapDescriptor? by remember { mutableStateOf(null) }

    LaunchedEffect(orientationAngle) {
        rotatedBitmap = viewModel.rotateBitmap(resizedBitmap, orientationAngle)
        rotatedBitmap?.let {
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(it)
        }
    }

    // State to control the visibility of the alert dialog
    var isLocationDialogVisible by remember { mutableStateOf(false) }

    // Flag to check if the dialog has been shown before
    var isDialogShownBefore by remember { mutableStateOf(false) }

    // Check if location services are enabled
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    if (!isLocationEnabled && !isDialogShownBefore) {
        // Location services are disabled and dialog has not been shown before, show alert dialog
        isLocationDialogVisible = true
        // Set the flag to true to indicate that the dialog has been shown
        isDialogShownBefore = true
    }

    if (isLocationDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog
                isLocationDialogVisible = false
            },
            title = { Text(text = "Location Services Disabled") },
            text = {
                Text(
                    text = "Please enable location services to use this feature.",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            delay(1500)
                            isLocationDialogVisible = false
                        }
                        // Open settings to enable location services
                        val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(settingsIntent)
                    }
                ) {
                    Text(text = "Turn on")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Dismiss the dialog
                        isLocationDialogVisible = false
                    }
                ) {
                    Text(text = "Dismiss")
                }
            }
        )
    }

    val sensorEventListener = remember {
        object : SensorEventListener {
            private val gravity = FloatArray(3)
            private val geomagnetic = FloatArray(3)
            private var lastUpdateTime = 0L

            override fun onSensorChanged(event: SensorEvent) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastUpdateTime < 100) { // Throttle updates to occur every 100 milliseconds
                    return
                }

                lastUpdateTime = currentTime

                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> gravity.apply { event.values.copyInto(this) }
                    Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic.apply { event.values.copyInto(this) }
                }

                val rotationMatrix = FloatArray(9)
                val success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)
                if (success) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    val azimuthRadians = orientation[0].toDouble()
                    azimuthDegrees = Math.toDegrees(azimuthRadians).toFloat()
                    orientationAngle = azimuthDegrees // Update the orientation angle
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    val launchMultiplePermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissionMaps ->
            val areGranted = permissionMaps.values.reduce { acc, next -> acc && next }
            if (areGranted) {
                viewModel.locationRequired = true
                viewModel.startLocationUpdates()
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(currentLocation) {
        if (viewModel.permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }) {

            //get location
            viewModel.startLocationUpdates()
        } else {
            launchMultiplePermissions.launch(viewModel.permissions)
        }
    }

    LaunchedEffect(orientationAngle) {
        orientationAngle = azimuthDegrees // Update the orientation angle
        // Delay to throttle updates
        delay(100) // Adjust the delay time as needed
    }

    DisposableEffect(Unit) {
        // Register sensor listeners with optimized sampling rate
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(sensorEventListener, magnetometerSensor, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            // Unregister sensor listeners
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            Marker(
                state = MarkerState(position = currentLocation),
                icon = bitmapDescriptor,
                title = "current location",
                snippet = "You are here!!!"
            )
        }
    }
}

