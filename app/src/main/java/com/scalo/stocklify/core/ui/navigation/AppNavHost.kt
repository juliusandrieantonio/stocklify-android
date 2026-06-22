package com.scalo.stocklify.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import com.scalo.stocklify.core.util.Screen
import com.scalo.stocklify.feature.onboarding.ui.OnboardingScreen
import com.scalo.stocklify.feature.stores.ui.CreateJoinStoreScreen
import com.scalo.stocklify.feature.stores.ui.CreateStoreScreen
import com.scalo.stocklify.feature.stores.ui.JoinStoreScreen

@Composable
fun AppNavHost(
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.CreateJoinStore.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            CreateJoinStoreScreen(
                onCreateStore = {
                    navController.navigate(Screen.CreateStore.route)
                },
                onJoinStore = {
                    navController.navigate(Screen.JoinStore.route)
                }
            )
        }

        composable(Screen.CreateJoinStore.route) {
            CreateJoinStoreScreen(
                onCreateStore = {
                    navController.navigate(Screen.CreateStore.route)
                },
                onJoinStore = {
                    navController.navigate(Screen.JoinStore.route)
                }
            )
        }

        composable(Screen.CreateStore.route) {
            CreateStoreScreen(
                onBack = { navController.popBackStack() },
                onCreated = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.CreateStore.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.JoinStore.route) {
            JoinStoreScreen(
                onBack = { navController.popBackStack() },
                onJoined = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.JoinStore.route) { inclusive = true }
                    }
                }
            )
        }


    }
}