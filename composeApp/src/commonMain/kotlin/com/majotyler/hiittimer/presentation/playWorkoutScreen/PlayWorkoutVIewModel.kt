package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayWorkoutVIewModel : ViewModel() {
    private val _state = MutableStateFlow(value = PlayWorkoutViewState(seconds = 0))
    val state = _state.asStateFlow()

    private val _play = MutableStateFlow(false)

    val play = _play.asStateFlow()
    private val _text = MutableStateFlow("Start")
    val text = _text.asStateFlow()

    private val _enabled = MutableStateFlow(true)
    val enabled = _enabled.asStateFlow()
    fun onEvent(event: PlayWorkoutViewEvent) {
        when (event) {
            is PlayWorkoutViewEvent.ClickedPlay -> onClickedPlay()
            is PlayWorkoutViewEvent.ClickedPause -> onClickedPause()
            is PlayWorkoutViewEvent.Enabled -> onEnabled()

        }
    }

    private fun onClickedPlay() {
        _play.value = !_play.value
        _text.value = if (_play.value) "Stop" else "Start"

        if (_play.value) {
            viewModelScope.launch {
                while (_play.value) {

                    _state.value = _state.value.copy(seconds = _state.value.seconds + 1)
                    delay(1000)

                }
            }
        }
    }

    private fun onClickedPause() {
        _play.value = false
        _text.value = "Start"
    }

    private fun onEnabled() {
        _enabled.value = false
    }
}