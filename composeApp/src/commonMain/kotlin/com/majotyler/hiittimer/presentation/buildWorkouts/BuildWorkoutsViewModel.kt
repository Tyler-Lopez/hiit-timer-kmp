package com.majotyler.hiittimer.presentation.buildWorkouts

import androidx.lifecycle.ViewModel
import com.majotyler.hiittimer.domain.model.Interval
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BuildWorkoutsViewModel(
    private val router: Router<BuildWorkoutsDestination>,
) : ViewModel() {

    private val _state = MutableStateFlow(
        value = BuildWorkoutsViewState(
            enabledButtonPlayWorkouts = true,
            workouts = listOf(
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
            )
        )
    )
    val state = _state.asStateFlow()

    fun onEvent(event: BuildWorkoutsViewEvent) {
        when (event) {
            is BuildWorkoutsViewEvent.AddedWorkout -> onAddedWorkout(event = event)
            is BuildWorkoutsViewEvent.ClickedAddWorkout -> onClickedAddWorkout()
            is BuildWorkoutsViewEvent.ClickedDeleteWorkout -> onClickedDeleteWorkout(event = event)
            is BuildWorkoutsViewEvent.ClickedGo -> onClickedGo()
        }
    }

    private fun onAddedWorkout(event: BuildWorkoutsViewEvent.AddedWorkout) {
        _state.update {
            it.copy(
                enabledButtonPlayWorkouts = true,
                workouts = it.workouts + event.workout,
            )
        }
    }

    private fun onClickedAddWorkout() {
        router.routeTo(destination = BuildWorkoutsDestination.NavigateToAddWorkout)
    }

    private fun onClickedDeleteWorkout(event: BuildWorkoutsViewEvent.ClickedDeleteWorkout) {
        _state.update {
            val workoutsNew = it.workouts
                .toMutableList()
                .apply {
                    removeAt(event.index)
                }

            it.copy(
                enabledButtonPlayWorkouts = workoutsNew.isNotEmpty(),
                workouts = workoutsNew,
            )
        }
    }

    private fun onClickedGo() {
        router.routeTo(
            destination = BuildWorkoutsDestination.NavigateToPlayWorkout(
                workouts = _state.value.workouts,
            ),
        )
    }
}
    