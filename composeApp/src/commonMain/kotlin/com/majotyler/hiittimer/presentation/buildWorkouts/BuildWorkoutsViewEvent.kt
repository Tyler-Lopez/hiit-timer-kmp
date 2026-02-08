package com.majotyler.hiittimer.presentation.buildWorkouts

import androidx.compose.runtime.Immutable
import com.majotyler.hiittimer.domain.model.Workout

@Immutable
sealed interface BuildWorkoutsViewEvent {
    data class AddedWorkout(val workout: Workout) : BuildWorkoutsViewEvent
    data object ClickedAddWorkout : BuildWorkoutsViewEvent
    data class ClickedDeleteWorkout(val index: Int) : BuildWorkoutsViewEvent
    data object ClickedGo: BuildWorkoutsViewEvent
}