package com.majotyler.hiittimer.presentation.homeScreen

import androidx.lifecycle.ViewModel
import com.majotyler.hiittimer.presentation.common.navigation.Router

class HomeViewModel(
    private val router: Router<HomeDestination>,
) : ViewModel() {

    fun onEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.ClickedLaunchBuildWorkouts -> onClickedLaunchBuildWorkouts()
        }
    }

    private fun onClickedLaunchBuildWorkouts() {
        router.routeTo(destination = HomeDestination.NavigateToBuildWorkouts)
    }
}
