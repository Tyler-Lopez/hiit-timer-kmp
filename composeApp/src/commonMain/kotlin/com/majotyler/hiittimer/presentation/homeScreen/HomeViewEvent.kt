package com.majotyler.hiittimer.presentation.homeScreen

sealed interface HomeViewEvent {
    data object ClickedConnectWithStrava : HomeViewEvent
    data object ClickedLaunchBuildWorkouts : HomeViewEvent
}