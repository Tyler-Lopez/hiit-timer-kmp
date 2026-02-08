package com.majotyler.hiittimer.presentation.homeScreen

import com.majotyler.hiittimer.presentation.common.navigation.Destination

sealed interface HomeDestination : Destination {
    data object NavigateToBuildWorkouts : HomeDestination
}