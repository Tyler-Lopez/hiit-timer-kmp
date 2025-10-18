package com.majotyler.hiittimer.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerViewModel : ViewModel() {
    private val _state = MutableStateFlow(value = TimerViewState(seconds = 0))
    val state = _state.asStateFlow()

    fun onEvent(event: TimerViewEvent) {
        when (event) {
            is TimerViewEvent.ClickedStartTimer -> onClickedStartTimer()
            is TimerViewEvent.ClickedStopTimer -> onClickedStopTimer()
        }
    }

    private fun onClickedStartTimer() {
        /** TODO (Tyler):
         * Start incrementing the seconds of the [_state].
         */
    }

    private fun onClickedStopTimer() {
        /** TODO (Tyler):
         * Stop incrementing the seconds of the [_state].
         */
    }
}