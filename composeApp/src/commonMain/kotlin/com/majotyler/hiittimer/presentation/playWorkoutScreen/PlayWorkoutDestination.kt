package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.compose.runtime.Immutable

@Immutable
sealed interface PlayWorkoutDestination {
    data object NavigateUp : PlayWorkoutDestination
}