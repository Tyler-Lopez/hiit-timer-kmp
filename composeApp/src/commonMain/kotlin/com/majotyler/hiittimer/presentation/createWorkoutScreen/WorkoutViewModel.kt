package com.majotyler.hiittimer.presentation.createWorkoutScreen

import androidx.lifecycle.ViewModel
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkoutViewModel(private val router: Router<WorkoutDestination>) : ViewModel() {
    private val _nameWorkout = MutableStateFlow("")
    val nameWorkout = _nameWorkout.asStateFlow()

    private val _enabled = MutableStateFlow(false)
    val enabled = _enabled.asStateFlow()

    private val _page = MutableStateFlow(value = AddWorkoutPage.entries.first())
    val page = _page.asStateFlow()

    fun onEvent(event: WorkoutViewEvent) {
        when (event) {
            is WorkoutViewEvent.NameWorkout -> onNameWorkout(event.newNameWorkout)
            is WorkoutViewEvent.ClickedAddExercise -> onClickedAddExercise()
            is WorkoutViewEvent.ClickedAdvance -> onClickedAdvance()
            is WorkoutViewEvent.ClickedNavigateUp -> onClickedNavigateUp()
        }
    }

    private fun onNameWorkout(newNameWorkout: String) {
        _nameWorkout.value = newNameWorkout
        _enabled.value = newNameWorkout.isNotBlank()
    }

    private fun onClickedAddExercise() {

    }

    private fun onClickedAdvance() {
        val nextPageOrdinal = page.value.ordinal + 1
        val wasOnLastPage = nextPageOrdinal > AddWorkoutPage.entries.lastIndex

        if (wasOnLastPage) {
            router.routeTo(
                destination = WorkoutDestination.AddWorkout(
                    workout = Workout(
                        intervals = emptyList(),
                        name = "TODO",
                        repetitions = 1,
                    )
                )
            )
        } else {
            _page.value = AddWorkoutPage.entries[nextPageOrdinal]
        }
    }

    private fun onClickedNavigateUp() {
        val nextPageOrdinal = page.value.ordinal - 1
        val wasOnFirstPage = nextPageOrdinal < 0

        if (wasOnFirstPage) {
            // TODO (This needs to navigate up so you go back to the previous screen, maybe with a
            // "are you sure" type message
        } else {
            _page.value = AddWorkoutPage.entries[nextPageOrdinal]
        }
    }
}