package com.majotyler.hiittimer.presentation.homeScreen

sealed interface HomeViewEvent {
    data object ClickedConnectWithStrava : HomeViewEvent

    // TODO This is temporary just to demonstrate
    data object ClickedCreateStravaActivity : HomeViewEvent
    data object ClickedLaunchBuildWorkouts : HomeViewEvent
}