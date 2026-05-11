package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majotyler.hiittimer.domain.model.Interval
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class PlayWorkoutVIewModel(
    private val router: Router<PlayWorkoutDestination>,
    private val workout: Workout,
) : ViewModel() {
    private val _state = MutableStateFlow(
        value = PlayWorkoutViewState(
            confirmationDialogVisible = false,
            progressDisplay = formatMs(ms = 0L),
            progress = 0F,
        )
    )
    val state = _state.asStateFlow()

    private val _play = MutableStateFlow(false)

    val play = _play.asStateFlow()
    private val _workoutPlayState = MutableStateFlow(value = WorkoutPlayState.PAUSED)
    val workoutPlayState = _workoutPlayState.asStateFlow()

    private val _enabled = MutableStateFlow(true)
    val enabled = _enabled.asStateFlow()

    private var currentStepProgressMs: Long = 0L
    private val currentStepTotalMs: Long
        get() = currentInterval
            ?.run {
                val seconds = when (currentIntervalState) {
                    PlayWorkoutStateOfInterval.EXERCISING -> duration
                    PlayWorkoutStateOfInterval.RESTING -> rest
                }
                val milliseconds = seconds * MILLISECONDS_IN_SECOND
                milliseconds
            } ?: 0L

    private var currentIntervalIndex: Int = 0
    private var currentIntervalState = PlayWorkoutStateOfInterval.EXERCISING
    private val currentInterval: Interval?
        get() = workout.intervals.getOrNull(index = currentIntervalIndex)

    private var pollProgressJob: Job? = null
    private var runningMark: TimeMark? = null

    fun onEvent(event: PlayWorkoutViewEvent) {
        when (event) {
            is PlayWorkoutViewEvent.ClickedDialogCancel -> onClickedDialogCancel()
            is PlayWorkoutViewEvent.ClickedDialogConfirm -> onClickedDialogConfirm()
            is PlayWorkoutViewEvent.ClickedPlay -> onClickedPlay()
            is PlayWorkoutViewEvent.ClickedPause -> onClickedPause()
            is PlayWorkoutViewEvent.ClickedSystemBack -> onClickedSystemBack()
        }
    }

    private fun onClickedDialogCancel() {
        _state.update {
            it.copy(confirmationDialogVisible = false)
        }
    }

    private fun onClickedDialogConfirm() {
        _state.update {
            it.copy(confirmationDialogVisible = false)
        }

        router.routeTo(destination = PlayWorkoutDestination.NavigateUp)
    }

    private fun onClickedPlay() {
        _play.value = !_play.value
        _workoutPlayState.value = if (_play.value) WorkoutPlayState.PLAYING else WorkoutPlayState.PAUSED

        if (_play.value) {
            if (runningMark == null) {
                runningMark = TimeSource.Monotonic.markNow()
            }

            pollProgressJob?.cancel()
            pollProgressJob = viewModelScope.launch {
                while (isActive) {
                    updateStateWithAccumulatedTime()
                    delay(timeMillis = UPDATE_DELAY_MS)
                }
            }
        }
    }

    private fun onClickedPause() {
        pauseWorkout()
    }

    private fun onClickedSystemBack() {
        pauseWorkout()

        _state.update {
            it.copy(
                confirmationDialogVisible = true,
            )
        }
    }

    private fun formatMs(ms: Long): String {
        val minutes = ms / MILLISECONDS_IN_MINUTE
        val seconds = (ms % MILLISECONDS_IN_MINUTE) / MILLISECONDS_IN_SECOND
        val centiseconds = (ms % MILLISECONDS_IN_SECOND) / MILLISECONDS_IN_CENTISECOND

        return minutes.toString().padStart(2, '0') +
                ":${seconds.toString().padStart(2, '0')}" +
                ":${centiseconds.toString().padStart(2, '0')}"
    }

    private fun pauseWorkout() {
        updateStateWithAccumulatedTime()
        runningMark = null
        pollProgressJob?.cancel()

        _play.value = false
        _workoutPlayState.value = WorkoutPlayState.PAUSED
    }

    private fun updateStateWithAccumulatedTime() {
        runningMark?.let {
            currentStepProgressMs = currentStepProgressMs
                .plus(other = it.elapsedNow().inWholeMilliseconds)
                .coerceAtMost(maximumValue = currentStepTotalMs)

            val progress: Float = (currentStepProgressMs / currentStepTotalMs.toFloat())

            _state.update { oldState ->
                oldState.copy(
                    progressDisplay = formatMs(ms = currentStepProgressMs),
                    progress = progress,
                ).also { newState ->

                    runningMark = TimeSource.Monotonic.markNow()

                    val hasReachedEndOfCurrentStep: Boolean = newState.progress >= 1F

                    if (hasReachedEndOfCurrentStep) {
                        val lastIndexOfIntervals = workout.intervals.lastIndex
                        val isResting = currentIntervalState == PlayWorkoutStateOfInterval.RESTING
                        val currentIntervalIsLast = currentIntervalIndex == lastIndexOfIntervals
                        val hasFinished = isResting && currentIntervalIsLast

                        if (hasFinished) {
                            _play.value = false
                            _workoutPlayState.value = WorkoutPlayState.PAUSED
                            _enabled.value = false
                            pollProgressJob?.cancel()

                        } else {
                            nextStep()
                        }
                    }
                }
            }
        }
    }

    private fun nextStep() {
        when (currentIntervalState) {
            PlayWorkoutStateOfInterval.EXERCISING -> {
                currentIntervalState = PlayWorkoutStateOfInterval.RESTING
            }

            PlayWorkoutStateOfInterval.RESTING -> {
                currentIntervalIndex++
                currentIntervalState = PlayWorkoutStateOfInterval.EXERCISING
            }
        }
        currentStepProgressMs = 0L
    }

    companion object {
        private const val UPDATE_DELAY_MS = 16L
        private const val MILLISECONDS_IN_CENTISECOND = 10L
        private const val MILLISECONDS_IN_MINUTE = 60_000L
        private const val MILLISECONDS_IN_SECOND = 1_000L
    }
}
