package com.majotyler.hiittimer.presentation.createWorkoutScreen

import com.majotyler.hiittimer.domain.model.Workout

sealed interface WorkoutDestination {
    data class AddWorkout(val workout: Workout) : WorkoutDestination
}