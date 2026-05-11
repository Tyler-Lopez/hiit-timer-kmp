package com.majotyler.hiittimer.presentation.createWorkoutScreen

import com.majotyler.hiittimer.domain.model.Workout

sealed interface CreateWorkoutDestination {
    data class CreatedWorkout(val workout: Workout) : CreateWorkoutDestination
    data object NavigateUp : CreateWorkoutDestination
}
