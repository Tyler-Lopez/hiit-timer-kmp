package com.majotyler.hiittimer.presentation.chooseWorkout

import androidx.compose.runtime.Immutable
import com.majotyler.hiittimer.domain.model.Workout

@Immutable
sealed interface ChooseWorkoutViewEvent {
    data class CreatedWorkout(val workout: Workout) : ChooseWorkoutViewEvent
    data object ClickedCreateWorkout : ChooseWorkoutViewEvent
    data object ClickedRemoveWorkout : ChooseWorkoutViewEvent
    data object ClickedGo : ChooseWorkoutViewEvent
}
