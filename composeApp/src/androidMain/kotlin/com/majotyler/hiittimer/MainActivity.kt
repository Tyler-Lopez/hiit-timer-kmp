package com.majotyler.hiittimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.majotyler.hiittimer.presentation.TimerScreen
import com.majotyler.hiittimer.presentation.TimerViewModel
import com.majotyler.hiittimer.presentation.common.navigation.NavigationRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                NavigationRoot()
            }
        }
    }
}