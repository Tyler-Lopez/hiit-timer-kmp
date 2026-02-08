package com.majotyler.hiittimer.presentation.buildWorkouts

import com.majotyler.hiittimer.domain.model.Workout

data class BuildWorkoutsViewState(
    val enabledButtonPlayWorkouts: Boolean,
    val workouts: List<Workout>,
)