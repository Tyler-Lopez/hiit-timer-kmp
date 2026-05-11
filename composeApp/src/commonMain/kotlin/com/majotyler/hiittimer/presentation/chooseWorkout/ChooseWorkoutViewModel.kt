package com.majotyler.hiittimer.presentation.chooseWorkout

import androidx.lifecycle.ViewModel
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChooseWorkoutViewModel(
    private val router: Router<ChooseWorkoutDestination>,
) : ViewModel() {

    private val _state = MutableStateFlow(
        value = ChooseWorkoutViewState(
            workout = null,
        )
    )
    val state = _state.asStateFlow()

    fun onEvent(event: ChooseWorkoutViewEvent) {
        when (event) {
            is ChooseWorkoutViewEvent.CreatedWorkout -> onCreatedWorkout(event = event)
            is ChooseWorkoutViewEvent.ClickedCreateWorkout -> onClickedCreateWorkout()
            is ChooseWorkoutViewEvent.ClickedRemoveWorkout -> onClickedRemoveWorkout()
            is ChooseWorkoutViewEvent.ClickedGo -> onClickedGo()
        }
    }

    private fun onCreatedWorkout(event: ChooseWorkoutViewEvent.CreatedWorkout) {
        _state.update {
            it.copy(workout = event.workout)
        }
    }

    private fun onClickedRemoveWorkout() {
        _state.update { it.copy(workout = null) }
    }

    private fun onClickedCreateWorkout() {
        router.routeTo(destination = ChooseWorkoutDestination.NavigateToCreateWorkout)
    }

    private fun onClickedGo() {
        val workout = _state.value.workout ?: return
        router.routeTo(
            destination = ChooseWorkoutDestination.NavigateToPlayWorkout(workout = workout),
        )
    }
}
