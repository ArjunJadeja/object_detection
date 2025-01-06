package com.arjunjadeja.objectdetection.ui.base

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arjunjadeja.objectdetection.ui.MainViewModel
import com.arjunjadeja.objectdetection.ui.screens.HomeScreen
import com.arjunjadeja.objectdetection.ui.screens.ObjectDetectionScreen

sealed class Route(val name: String) {
    data object HomeScreen : Route(name = "home_screen")
    data object ObjectDetectionScreen : Route(name = "object_detection_screen")
}

@Composable
fun AppNavHost(mainViewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.HomeScreen.name
    ) {
        composable(route = Route.HomeScreen.name) {
            HomeScreen(
                mainViewModel = mainViewModel,
                navController = navController
            )
        }
        composable(route = Route.ObjectDetectionScreen.name) {
            ObjectDetectionScreen(
                mainViewModel = mainViewModel,
                navController = navController
            )
        }
    }
}