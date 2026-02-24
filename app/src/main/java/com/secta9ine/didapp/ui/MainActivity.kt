package com.secta9ine.didapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import android.view.WindowManager
import com.secta9ine.didapp.ui.theme.DidAppTheme
import com.secta9ine.didapp.ui.viewmodel.DidV2ViewModel
import com.secta9ine.didapp.ui.components.TextContent
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

        setContent {
            DidAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: DidV2ViewModel = hiltViewModel()
                    val snapshot = viewModel.snapshot.collectAsState().value
                    if (snapshot != null) {
                        DynamicDidScreenV2(snapshot = snapshot)
                    } else {
                        TextContent(text = "Waiting for snapshot...")
                    }
                }
            }
        }
    }
}
