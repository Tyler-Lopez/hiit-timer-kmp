package com.majotyler.hiittimer.presentation.createWorkoutScreen

import androidx.lifecycle.ViewModel
import com.majotyler.hiittimer.domain.model.Interval
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateWorkoutViewModel(private val router: Router<CreateWorkoutDestination>) : ViewModel() {
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

    private val _page = MutableStateFlow(value = CreateWorkoutPage.entries.first())
    val page = _page.asStateFlow()

    private val _workoutRepetitions = MutableStateFlow(value = 1)
    val workoutRepetitions = _workoutRepetitions.asStateFlow()

    private val _secondsDuration = MutableStateFlow(value = 0)
    val secondsDuration = _secondsDuration.asStateFlow()

    private val _secondsRest = MutableStateFlow(value = 0)
    val secondsRest = _secondsRest.asStateFlow()

    fun onEvent(event: CreateWorkoutViewEvent) {
        when (event) {
            is CreateWorkoutViewEvent.ChangedIntervalDuration -> onChangedIntervalDuration(event = event)
            is CreateWorkoutViewEvent.ChangedIntervalName -> onChangedIntervalName(event = event)
            is CreateWorkoutViewEvent.ChangedIntervalRest -> onChangedIntervalRest(event = event)
            is CreateWorkoutViewEvent.ChangedWorkoutName -> onChangedWorkoutName(event = event)
            is CreateWorkoutViewEvent.ClickedAddIntervalToWorkout -> onClickedAddIntervalToWorkout()
            is CreateWorkoutViewEvent.ClickedAdvance -> onClickedAdvance()
            is CreateWorkoutViewEvent.ClickedDeleteInterval -> onClickedDeleteInterval(event = event)
            is CreateWorkoutViewEvent.ClickedNavigateUp -> onClickedNavigateUp()
            is CreateWorkoutViewEvent.WorkoutRepetitionsDecreased -> onWorkoutRepetitionsDecreased()
            is CreateWorkoutViewEvent.WorkoutRepetitionsIncreased -> onWorkoutRepetitionsIncreased()
        }
    }

    private fun onChangedIntervalDuration(event: CreateWorkoutViewEvent.ChangedIntervalDuration) {
        _secondsDuration.value = secondsStringToInteger(seconds = event.seconds)
        checkAddExerciseButtonEnabled()
    }

    private fun onChangedIntervalName(event: CreateWorkoutViewEvent.ChangedIntervalName) {
        _nameInterval.value = event.name
        checkAddExerciseButtonEnabled()
    }

    private fun onChangedIntervalRest(event: CreateWorkoutViewEvent.ChangedIntervalRest) {
        _secondsRest.value = secondsStringToInteger(seconds = event.seconds)
        checkAddExerciseButtonEnabled()
    }

    private fun onChangedWorkoutName(event: CreateWorkoutViewEvent.ChangedWorkoutName) {
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
        val wasOnLastPage = nextPageOrdinal > CreateWorkoutPage.entries.lastIndex

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
            _page.value = CreateWorkoutPage.entries[nextPageOrdinal]
        }

        checkAdvanceButtonEnabled()
    }

    private fun onClickedDeleteInterval(event: CreateWorkoutViewEvent.ClickedDeleteInterval) {
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
            _page.value = CreateWorkoutPage.entries[nextPageOrdinal]
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

    private fun checkAddExerciseButtonEnabled() {
        val exerciseHasAName = _nameInterval.value.isNotBlank()
        val durationAndRestAreNonZero = _secondsDuration.value != 0 && _secondsRest.value != 0
        _enabledAddInterval.value = exerciseHasAName && durationAndRestAreNonZero
    }

    private fun checkAdvanceButtonEnabled() {
        fun onAddInterval(): Boolean = intervals.value.isNotEmpty()
        fun onNameWorkout(): Boolean = nameWorkout.value.isNotBlank()
        fun onSelectReps(): Boolean = true

        _enabledAdvance.value = when (_page.value) {
            CreateWorkoutPage.ADD_INTERVAL -> onAddInterval()
            CreateWorkoutPage.NAME_WORKOUT -> onNameWorkout()
            CreateWorkoutPage.SELECT_REPS -> onSelectReps()
        }
    }

    private fun checkWorkoutRepetitionsDecreasedButtonEnabled() {
        _enabledDecreaseRepetitions.value = workoutRepetitions.value > 1
    }

    private fun secondsStringToInteger(seconds: String): Int {
        return seconds.toIntOrNull()?.coerceAtLeast(minimumValue = 0) ?: 0
    }
}
