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
    private val workouts: List<Workout>,
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
    private val _text = MutableStateFlow("Start")
    val text = _text.asStateFlow()

    private val _enabled = MutableStateFlow(true)
    val enabled = _enabled.asStateFlow()

    private var currentStepProgressMs: Long = 0L
    private val currentStepTotalMs: Long // Cada vez que utilizo currentStepTotalMs el codigo de abajo se ejecuta, todo el valor de abajo se guardara momentaneamente en currentStepTotalMs
        get() = currentWorkoutInterval
            ?.run{
                val seconds = when (currentWorkoutIntervalState) { // Asigna a Seconds la duracion o el descanso
                    PlayWorkoutStateOfInterval.EXERCISING -> duration
                    PlayWorkoutStateOfInterval.RESTING -> rest
                }
                val milliseconds = seconds * MILLISECONDS_IN_SECOND
                milliseconds
            } ?: 0L
    // get() = currentWorkoutInterval
    //            ?.run{ this -> // puedes hacer this.duration o duration porque this por que es un apuntador ya esta en currentWotkoutInterval es que es de tipo Interval
    //              Interval es @Serializable  data class Interval(
    //    val duration: Int,
    //    val name: String,
    //    val rest: Int,
    //)
    //                val seconds = when (currentWorkoutIntervalState) {
    //                    PlayWorkoutStateOfInterval.EXERCISING -> this.duration
    //                    PlayWorkoutStateOfInterval.RESTING -> this.rest
    //                }
    //                val milliseconds = seconds * MILLISECONDS_IN_SECOND
    //                milliseconds
    //            } ?: 0L
//    fun currentStepTotal(): Long {
////        val currentWorkoutIntervalInmutable = currentWorkoutInterval
//        if (currentWorkoutInterval!= null){
//            currentWorkoutIntervalIndex = -50
//            val seconds = when (currentWorkoutIntervalState) {
//                PlayWorkoutStateOfInterval.EXERCISING -> currentWorkoutInterval.duration
//                PlayWorkoutStateOfInterval.RESTING -> currentWorkoutInterval.rest
//            }
//            val milliseconds = seconds * MILLISECONDS_IN_SECOND
//            return milliseconds
//        }else{
//            return 0L
//        }
//    }

    /** The index of the current [Workout] in [workouts]. */
    private var currentWorkoutIndex: Int = 0
    private val currentWorkout: Workout?
        get() = workouts.getOrNull(index = currentWorkoutIndex) // obtiene el valor de un workout es de tipo Workout entonces tiene que verse como eso

    /** The index of the current [Interval] of [currentWorkout] */
    private var currentWorkoutIntervalIndex: Int = 0
    private var currentWorkoutIntervalState = PlayWorkoutStateOfInterval.EXERCISING // si hicieramos un print aqui diria 'EXERCISING' porque tiene un nombre interno
    private val currentWorkoutInterval: Interval? // de tipo interval
        get() = currentWorkout?.intervals?.getOrNull(index = currentWorkoutIntervalIndex)
        // si no es null currentWorkout  -> si no es null intervals -> entra a intervals que es una lista de tipo interval que es una data class
        // se veria algo como
        // data class Workout(
        //    val intervals: List<Interval>,
        //    val name: String,
        //    val repetitions: Int, )

        //      intervals = listOf(
        //        Interval(30, "Jumping Jacks", 10),
        //        Interval(40, "Push Ups", 15),
        //        Interval(60, "Squats", 20)
        //    )
        //    y con el indice obten un interval
//    fun CurrentWorkoutInterval(): Interval?{
//        return currentWorkout?.intervals?.getOrNull(index = currentWorkoutIntervalIndex)
//    }
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
                        val lastIndexOfIntervalsInCurrentWorkout = currentWorkout?.intervals?.lastIndex
                        val isResting = currentWorkoutIntervalState == PlayWorkoutStateOfInterval.RESTING
                        val currentIntervalIsLastInWorkout = currentWorkoutIntervalIndex == lastIndexOfIntervalsInCurrentWorkout
                        val hasFinishedAllWorkouts = isResting && currentIntervalIsLastInWorkout


                       if (hasFinishedAllWorkouts){
                           _play.value = false
                           _text.value = "Start"
                           _enabled.value = false
                           pollProgressJob?.cancel()
                       }else {
                           nextStep()
                       }

                    }
                }
            }
        }
    }

    private fun nextStep(){
        when (currentWorkoutIntervalState){
            PlayWorkoutStateOfInterval.EXERCISING -> { currentWorkoutIntervalState =
                PlayWorkoutStateOfInterval.RESTING
            }
            PlayWorkoutStateOfInterval.RESTING -> {
                currentWorkoutIntervalIndex++
                currentWorkoutIntervalState =
                    PlayWorkoutStateOfInterval.EXERCISING
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