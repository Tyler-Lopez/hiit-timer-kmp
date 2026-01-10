package com.majotyler.hiittimer.presentation

import androidx.compose.runtime.Immutable
import com.majotyler.hiittimer.domain.model.Workout

@Immutable
sealed interface TimerViewEvent {
    data class AddedWorkout(val workout: Workout) : TimerViewEvent
    data object ClickedAddWorkout : TimerViewEvent
    data class ClickedDeleteWorkout(val index: Int) : TimerViewEvent
    data object ClickedGo: TimerViewEvent
}