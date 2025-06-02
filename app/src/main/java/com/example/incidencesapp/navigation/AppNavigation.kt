// navigation/AppNavigation.kt
package com.example.incidencesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.incidencesapp.screens.*

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onNavigateToCreate = {
                    navController.navigate("create_incidence")
                },
                onNavigateToEdit = { incidenceId ->
                    navController.navigate("edit_incidence/$incidenceId")
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("create_incidence") {
            CreateEditIncidenceScreen(
                incidenceId = null,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("edit_incidence/{incidenceId}") { backStackEntry ->
            val incidenceId = backStackEntry.arguments?.getString("incidenceId")
            CreateEditIncidenceScreen(
                incidenceId = incidenceId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}