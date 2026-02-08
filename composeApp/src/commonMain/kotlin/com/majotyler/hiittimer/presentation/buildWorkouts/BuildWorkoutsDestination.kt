package com.majotyler.hiittimer.presentation.buildWorkouts

import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.navigation.Destination

sealed interface BuildWorkoutsDestination : Destination {
    data object NavigateToAddWorkout : BuildWorkoutsDestination
    data class NavigateToPlayWorkout(
        val workouts: List<Workout>,
    ) : BuildWorkoutsDestination
}