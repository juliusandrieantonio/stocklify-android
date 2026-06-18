package com.scalo.stocklify.feature.splash.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.scalo.stocklify.core.ui.navigation.AppNavHost
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenView.view, View.ALPHA, 1f, 0f
            )
            fadeOut.duration = 400
            fadeOut.doOnEnd { splashScreenView.remove() }
            fadeOut.start()
        }

        setContent {
            StocklifyTheme {
                val isLoading by viewModel.isLoading.collectAsState()
                val startDestination by viewModel.startDestination.collectAsState()

                if (!isLoading && startDestination != null) {
                    AppNavHost(startDestination = startDestination!!)
                }
            }
        }
    }
}