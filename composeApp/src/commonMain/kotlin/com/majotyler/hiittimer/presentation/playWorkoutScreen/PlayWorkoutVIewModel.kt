package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayWorkoutVIewModel : ViewModel() {
    private val _state = MutableStateFlow(value = PlayWorkoutViewState(seconds = 0))
    val state = _state.asStateFlow()

    fun onEvent(event: PlayWorkoutViewEvent) {
        when (event) {
            is PlayWorkoutViewEvent.ClickedPlay -> onClickedPlay()
            is PlayWorkoutViewEvent.ClickedPause -> onClickedPause()
        }
    }

    private fun onClickedPlay() {
        /** TODO (Tyler):
         * Start incrementing the seconds of the [_state].
         */
    }

    private fun onClickedPause() {
        /** TODO (Tyler):
         * Stop incrementing the seconds of the [_state].
         */
    }
}