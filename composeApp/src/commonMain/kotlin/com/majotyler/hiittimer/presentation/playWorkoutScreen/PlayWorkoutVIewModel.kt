package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majotyler.hiittimer.presentation.addWorkoutScreen.AddWorkoutDestination
import com.majotyler.hiittimer.presentation.common.navigation.Destination
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

class PlayWorkoutVIewModel(private val router: Router<PlayWorkoutDestination>) : ViewModel() {
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
    private val _text = MutableStateFlow("Start")
    val text = _text.asStateFlow()

    private val _enabled = MutableStateFlow(true)
    val enabled = _enabled.asStateFlow()

    private var currentStepProgressMs: Long = 0L
    private var currentStepTotalMs: Long = 0L

    private var pollProgressJob: Job? = null
    private var runningMark: TimeMark? = null

    init {
        // TODO: Replace with real step length
        currentStepTotalMs = 10_000L
    }

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
        _text.value = if (_play.value) "Stop" else "Start"

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
        _text.value = "Start"
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
                        _play.value = false
                        _text.value = "Start"
                        _enabled.value = false
                        pollProgressJob?.cancel()
                    }
                }
            }
        }
    }

    companion object {
        private const val UPDATE_DELAY_MS = 16L
        private const val MILLISECONDS_IN_CENTISECOND = 10L
        private const val MILLISECONDS_IN_MINUTE = 60_000L
        private const val MILLISECONDS_IN_SECOND = 1_000L
    }
}