package com.majotyler.hiittimer.presentation.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.majotyler.hiittimer.presentation.common.navigation.Router

sealed interface HomeViewEvent {
    data object ClickedLaunchTimer : HomeViewEvent
}