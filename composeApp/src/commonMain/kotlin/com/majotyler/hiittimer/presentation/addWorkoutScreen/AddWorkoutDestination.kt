package com.majotyler.hiittimer.presentation.addWorkoutScreen

import com.majotyler.hiittimer.domain.model.Workout

sealed interface AddWorkoutDestination {
    data class AddWorkout(val workout: Workout) : AddWorkoutDestination
    data object NavigateUp : AddWorkoutDestination
}