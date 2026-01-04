package com.majotyler.hiittimer.presentation.createWorkoutScreen

import androidx.lifecycle.ViewModel
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkoutViewModel(private val router: Router<WorkoutDestination>) : ViewModel() {
    private val _nameWorkout = MutableStateFlow("")
    val nameWorkout = _nameWorkout.asStateFlow()

    private val _enabled = MutableStateFlow(false)
    val enabled = _enabled.asStateFlow()

    private val _page = MutableStateFlow(value = CreateWorkoutPage.entries.first())
    val page = _page.asStateFlow()

    fun onEvent(event: WorkoutViewEvent) {
        when (event) {
            is WorkoutViewEvent.NameWorkout -> onNameWorkout(event.newNameWorkout)
            is WorkoutViewEvent.AddWorkout -> onAddWorkout()
            is WorkoutViewEvent.ClickedAdvanceButton -> onClickedAdvanceButton()
            is WorkoutViewEvent.ClickedNavigateUp -> onClickedNavigateUp()
        }
    }

    private fun onNameWorkout(newNameWorkout: String) {
        _nameWorkout.value = newNameWorkout
        _enabled.value = newNameWorkout.isNotBlank()
    }

    private fun onAddWorkout() {
        router.routeTo(destination = WorkoutDestination.NavigateToTimer)
    }

    private fun onClickedAdvanceButton() {
        val newOrdinal = _page.value.ordinal + 1

        if (newOrdinal <= CreateWorkoutPage.entries.lastIndex) {
            _page.value = CreateWorkoutPage.entries.get(index = newOrdinal)
        } else {
            // TODO, this indicates we should create and add the workout
        }
    }

    private fun onClickedNavigateUp() {
        val newOrdinal = _page.value.ordinal - 1

        if (newOrdinal >= 0) {
            _page.value = CreateWorkoutPage.entries.get(index = newOrdinal)
        }
    }
}