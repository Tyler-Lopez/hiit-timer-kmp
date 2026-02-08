package com.majotyler.hiittimer.presentation.homeScreen

sealed interface HomeViewEvent {
    data object ClickedLaunchBuildWorkouts : HomeViewEvent
}