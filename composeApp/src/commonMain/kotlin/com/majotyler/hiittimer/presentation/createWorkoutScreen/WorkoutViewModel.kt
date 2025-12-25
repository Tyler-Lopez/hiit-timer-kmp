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

    fun onEvent(event: WorkoutViewEvent) {
        when (event) {
            is WorkoutViewEvent.NameWorkout -> onNameWorkout(event.newNameWorkout)
            is WorkoutViewEvent.AddWorkout -> onAddWorkout()

        }
    }

    fun onNameWorkout(newNameWorkout: String) {
        _nameWorkout.value = newNameWorkout
        _enabled.value = newNameWorkout.isNotBlank()
    }

    fun onAddWorkout() {
        router.routeTo(destination = WorkoutDestination.NavigateToTimer)

    }
}