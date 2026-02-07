package com.majotyler.hiittimer.presentation

import androidx.lifecycle.ViewModel
import com.majotyler.hiittimer.domain.model.Interval
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerViewModel(
    private val router: Router<TimerDestination>,
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _workouts = MutableStateFlow(value = listOf(
        Workout(
            intervals = listOf(
                Interval(
                    duration = 10,
                    name = "Interval 1",
                    rest = 5,
                ),
                Interval(
                    duration = 20,
                    name = "Interval 2",
                    rest = 30,
                ),
            ),
            name = "Workout 1",
            repetitions = 1,
        )
    ))

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

    private fun onClickedGo() {
        router.routeTo(
            destination = TimerDestination.NavigateToPlayWorkout(
                workouts = workouts.value,
            ),
        )
    }
}
    