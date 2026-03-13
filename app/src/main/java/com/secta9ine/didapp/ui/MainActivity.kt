package com.secta9ine.didapp.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.secta9ine.didapp.system.PowerScheduleReceiver
import com.secta9ine.didapp.ui.components.TextContent
import com.secta9ine.didapp.ui.theme.DidAppTheme
import com.secta9ine.didapp.ui.viewmodel.DidV2ViewModel
import com.secta9ine.didapp.v2.ui.DynamicDidScreenV2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Check if launched during sleep window via SharedPreferences
        val sleepPrefs = getSharedPreferences(PowerScheduleReceiver.PREFS_NAME, MODE_PRIVATE)
        val initialSleeping = sleepPrefs.getBoolean(PowerScheduleReceiver.KEY_IS_SLEEPING, false)

        setContent {
            DidAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: DidV2ViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.collectAsState().value
                    val snapshot = viewModel.snapshot.collectAsState().value
                    val isSleeping = viewModel.isSleeping.collectAsState().value

                    // Apply sleep mode from SharedPreferences on first launch
                    LaunchedEffect(Unit) {
                        if (initialSleeping) {
                            viewModel.onPowerStateChanged(true)
                        }
                    }

                    // Handle screen brightness based on sleep state
                    LaunchedEffect(isSleeping) {
                        val layoutParams = window.attributes
                        if (isSleeping) {
                            layoutParams.screenBrightness = 0f
                            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        } else {
                            layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        }
                        window.attributes = layoutParams
                    }

                    if (isSleeping) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                        )
                    } else {
                        when (uiState.stage) {
                            DidV2ViewModel.Stage.CHECKING_DEVICE -> {
                                TextContent(text = "Checking device status...")
                            }
                            DidV2ViewModel.Stage.PENDING_APPROVAL -> {
                                TextContent(text = uiState.message ?: "Waiting for approval...")
                            }
                            DidV2ViewModel.Stage.BLOCKED -> {
                                TextContent(text = uiState.message ?: "Device blocked")
                            }
                            DidV2ViewModel.Stage.ERROR -> {
                                TextContent(text = uiState.message ?: "Device authentication error")
                            }
                            DidV2ViewModel.Stage.READY -> {
                                if (snapshot != null) {
                                    DynamicDidScreenV2(snapshot = snapshot)
                                } else {
                                    TextContent(text = "Loading latest snapshot...")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
