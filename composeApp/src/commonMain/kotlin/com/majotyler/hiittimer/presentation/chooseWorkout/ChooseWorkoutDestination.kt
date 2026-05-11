package com.majotyler.hiittimer.presentation.chooseWorkout

import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.navigation.Destination

sealed interface ChooseWorkoutDestination : Destination {
    data object NavigateToCreateWorkout : ChooseWorkoutDestination
    data class NavigateToPlayWorkout(
        val workout: Workout,
    ) : ChooseWorkoutDestination
}
