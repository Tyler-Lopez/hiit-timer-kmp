package com.majotyler.hiittimer.presentation

/**
 * An encapsulation of the UI state required to render a timer which may be incremented.
 *
 * @param seconds The number of seconds which the timer has been incrementing for.
 */
data class TimerViewState(
    val seconds: Int,
)