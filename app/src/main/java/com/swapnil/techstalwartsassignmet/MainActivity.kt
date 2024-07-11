package com.swapnil.techstalwartsassignmet

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.swapnil.techstalwartsassignmet.ui.theme.TechstalwartsAssignmetTheme
import com.swapnil.techstalwartsassignmet.ui.view.LocationScreen
import com.swapnil.techstalwartsassignmet.ui.view.LoginScreen
import com.swapnil.techstalwartsassignmet.ui.view.SplashScreen
import com.swapnil.techstalwartsassignmet.ui.view.UserScreen
import com.swapnil.techstalwartsassignmet.viewmodel.LocationViewModel
import com.swapnil.techstalwartsassignmet.viewmodel.LoginViewModel
import com.swapnil.techstalwartsassignmet.viewmodel.LoginViewModelFactory
import com.swapnil.techstalwartsassignmet.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TechstalwartsAssignmetTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash_screen") {

                    composable("splash_screen") {
                        SplashScreen(navController,viewModel)
                    }
                    composable("login_screen") {
                        LoginScreen(navController = navController,viewModel)
                    }
                    composable("user_screen") {
                        UserScreen(navController = navController, loginViewModel = viewModel)
                    }
                }
            }
        }
    }
}

