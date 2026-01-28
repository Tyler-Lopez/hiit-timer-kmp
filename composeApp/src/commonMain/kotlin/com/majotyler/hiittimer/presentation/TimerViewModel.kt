package com.majotyler.hiittimer.presentation

import androidx.lifecycle.ViewModel
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerViewModel(
    private val router: Router<TimerDestination>,
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _workouts = MutableStateFlow(value = emptyList<Workout>())
    val workouts = _workouts.asStateFlow()

    private val _enabled = MutableStateFlow(false)
    val enabled = _enabled.asStateFlow()

    fun onEvent(event: TimerViewEvent) {
        when (event) {
            is TimerViewEvent.AddedWorkout -> onAddedWorkout(event = event)
            is TimerViewEvent.ClickedAddWorkout -> onClickedAddWorkout()
            is TimerViewEvent.ClickedDeleteWorkout -> onClickedDeleteWorkout(event.index)
            is TimerViewEvent.ClickedGo -> onClickedGo()
        }
    }

    private fun onAddedWorkout(event: TimerViewEvent.AddedWorkout) {
        _workouts.value += event.workout
    }

    private fun onClickedAddWorkout() {
        router.routeTo(destination = TimerDestination.NavigateToAddWorkout)
    }

    private fun onClickedDeleteWorkout(index: Int) {
        _workouts.value = _workouts.value.toMutableList().apply {
            removeAt(index)
        }
    }

    private fun onClickedGo(){
        router.routeTo(TimerDestination.NavigateToPlayWorkout)
    }
}
    