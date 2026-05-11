package com.majotyler.hiittimer.presentation.addWorkoutScreen

import androidx.lifecycle.ViewModel
import com.majotyler.hiittimer.domain.model.Interval
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddWorkoutViewModel(private val router: Router<CreateWorkoutDestination>) : ViewModel() {
    private val _nameInterval = MutableStateFlow(value = "")
    val nameInterval = _nameInterval.asStateFlow()

    private val _nameWorkout = MutableStateFlow(value = "")
    val nameWorkout = _nameWorkout.asStateFlow()

    private val _enabledAddInterval = MutableStateFlow(value = false)
    val enabledAddInterval = _enabledAddInterval.asStateFlow()

    private val _enabledAdvance = MutableStateFlow(value = false)
    val enabledAdvance = _enabledAdvance.asStateFlow()

    private val _enabledDecreaseRepetitions = MutableStateFlow(value = false)
    val enabledDecreaseRepetitions = _enabledDecreaseRepetitions.asStateFlow()

    private val _intervals = MutableStateFlow(value = emptyList<Interval>())
    val intervals = _intervals.asStateFlow()

    private val _page = MutableStateFlow(value = AddWorkoutPage.entries.first())
    val page = _page.asStateFlow()

    private val _workoutRepetitions = MutableStateFlow(value = 1)
    val workoutRepetitions = _workoutRepetitions.asStateFlow()

    private val _secondsDuration = MutableStateFlow(value = 0)
    val secondsDuration = _secondsDuration.asStateFlow()

    private val _secondsRest = MutableStateFlow(value = 0)
    val secondsRest = _secondsRest.asStateFlow()

    fun onEvent(event: AddWorkoutViewEvent) {
        when (event) {
            is AddWorkoutViewEvent.ChangedIntervalDuration -> onChangedIntervalDuration(event = event)
            is AddWorkoutViewEvent.ChangedIntervalName -> onChangedIntervalName(event = event)
            is AddWorkoutViewEvent.ChangedIntervalRest -> onChangedIntervalRest(event = event)
            is AddWorkoutViewEvent.ChangedWorkoutName -> onChangedWorkoutName(event = event)
            is AddWorkoutViewEvent.ClickedAddIntervalToWorkout -> onClickedAddIntervalToWorkout()
            is AddWorkoutViewEvent.ClickedAdvance -> onClickedAdvance()
            is AddWorkoutViewEvent.ClickedDeleteInterval -> onClickedDeleteInterval(event = event)
            is AddWorkoutViewEvent.ClickedNavigateUp -> onClickedNavigateUp()
            is AddWorkoutViewEvent.WorkoutRepetitionsDecreased -> onWorkoutRepetitionsDecreased()
            is AddWorkoutViewEvent.WorkoutRepetitionsIncreased -> onWorkoutRepetitionsIncreased()
        }
    }

    private fun onChangedIntervalDuration(event: AddWorkoutViewEvent.ChangedIntervalDuration) {
        _secondsDuration.value = secondsStringToInteger(seconds = event.seconds)
        checkAddExerciseButtonEnabled()
    }

    private fun onChangedIntervalName(event: AddWorkoutViewEvent.ChangedIntervalName) {
        _nameInterval.value = event.name
        checkAddExerciseButtonEnabled()
    }

    private fun onChangedIntervalRest(event: AddWorkoutViewEvent.ChangedIntervalRest) {
        _secondsRest.value = secondsStringToInteger(seconds = event.seconds)
        checkAddExerciseButtonEnabled()
    }

    private fun onChangedWorkoutName(event: AddWorkoutViewEvent.ChangedWorkoutName) {
        _nameWorkout.value = event.name

        checkAdvanceButtonEnabled()
    }

    private fun onClickedAddIntervalToWorkout() {
        _intervals.value += Interval(
            duration = _secondsDuration.value,
            name = _nameInterval.value,
            rest = _secondsRest.value,
        )

        _nameInterval.value = ""
        _secondsDuration.value = 0
        _secondsRest.value = 0

        _enabledAddInterval.value = false

        checkAdvanceButtonEnabled()
    }

    private fun onClickedAdvance() {
        val nextPageOrdinal = page.value.ordinal + 1
        val wasOnLastPage = nextPageOrdinal > AddWorkoutPage.entries.lastIndex

        if (wasOnLastPage) {
            router.routeTo(
                destination = CreateWorkoutDestination.CreatedWorkout(
                    workout = Workout(
                        intervals = intervals.value,
                        name = nameWorkout.value,
                        repetitions = workoutRepetitions.value,
                    )
                )
            )
        } else {
            _page.value = AddWorkoutPage.entries[nextPageOrdinal]
        }

        checkAdvanceButtonEnabled()
    }

    private fun onClickedDeleteInterval(event: AddWorkoutViewEvent.ClickedDeleteInterval) {
        val newList = _intervals.value.toMutableList()
        newList.removeAt(index = event.index)
        _intervals.value = newList

        checkAdvanceButtonEnabled()
    }

    private fun onClickedNavigateUp() {
        val nextPageOrdinal = page.value.ordinal - 1
        val wasOnFirstPage = nextPageOrdinal < 0

        if (wasOnFirstPage) {
            router.routeTo(destination = CreateWorkoutDestination.NavigateUp)
        } else {
            _page.value = AddWorkoutPage.entries[nextPageOrdinal]
        }
    }

    private fun onWorkoutRepetitionsDecreased() {
        _workoutRepetitions.value -= 1

        checkWorkoutRepetitionsDecreasedButtonEnabled()
    }

    private fun onWorkoutRepetitionsIncreased() {
        _workoutRepetitions.value += 1

        checkWorkoutRepetitionsDecreasedButtonEnabled()
    }

    //region Utility functions
    private fun checkAddExerciseButtonEnabled() {
        val exerciseHasAName = _nameInterval.value.isNotBlank()
        val durationAndRestAreNonZero = _secondsDuration.value != 0 && _secondsRest.value != 0

        _enabledAddInterval.value = exerciseHasAName && durationAndRestAreNonZero
    }

    private fun checkAdvanceButtonEnabled() {
        fun onAddInterval(): Boolean {
            return intervals.value.isNotEmpty()
        }

        fun onNameWorkout(): Boolean {
            return nameWorkout.value.isNotBlank()
        }

        fun onSelectReps(): Boolean {
            return true
        }

        _enabledAdvance.value = when (_page.value) {
            AddWorkoutPage.ADD_INTERVAL -> onAddInterval()
            AddWorkoutPage.NAME_WORKOUT -> onNameWorkout()
            AddWorkoutPage.SELECT_REPS -> onSelectReps()
        }
    }

    private fun checkWorkoutRepetitionsDecreasedButtonEnabled() {
        _enabledDecreaseRepetitions.value = workoutRepetitions.value > 1
    }

    private fun secondsStringToInteger(seconds: String): Int {
        return seconds.toIntOrNull()?.coerceAtLeast(minimumValue = 0) ?: 0
    }
    //endregion Utility functions
}