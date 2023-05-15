package com.example.weathertoday.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.weathertoday.presentation.util.SetupNavigation
import com.example.weathertoday.ui.theme.WeatherTodayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {


        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.d(
                        "Tag",
                        "All Permissions Granted"
                    )
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.d(
                        "Tag",
                        "Coarse LocationGranted"
                    )
                }

                else -> {
                    Log.d(
                        "Tag",
                        "None"
                    )
                }
            }


        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )

        super.onCreate(savedInstanceState)
        setContent {
            WeatherTodayTheme {
                SetupNavigation()

            }
        }
    }


}


@Composable
fun PermissionNotGranted() {
    Text(text = "PermissionNotGranted")
}