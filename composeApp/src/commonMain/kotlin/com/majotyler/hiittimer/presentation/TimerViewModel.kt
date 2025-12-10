package com.majotyler.hiittimer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _exercises =
        MutableStateFlow(listOf("Lagartijas", "gallitos", "dominadas", " sentadillas"))
    val exercises = _exercises.asStateFlow()

    private val _reps = MutableStateFlow(0)
    val reps = _reps.asStateFlow()

    fun onEvent(event: TimerViewEvent) {
        when (event) {
            is TimerViewEvent.ClickedAdd -> {
                viewModelScope.launch {
                    onClickedAdd()
                }
            }

            is TimerViewEvent.ClickedDelete -> {
                onClickedDelete(event.index)
            }

            is TimerViewEvent.AddReps -> {
                addReps()
            }

            is TimerViewEvent.RemoveReps -> {
                removeReps()
            }

        }
    }

    private suspend fun onClickedAdd() {
        _loading.value = true
        delay(4000)
        _loading.value = false
    }

    private fun onClickedDelete(index: Int) {
        _exercises.value = _exercises.value.toMutableList().apply {
            removeAt(index)
        }
    }

    private fun addReps() {
        _reps.value++
    }

    private fun removeReps() {
        if (_reps.value >0) _reps.value--
    }

}

//  private val _state = MutableStateFlow(value = TimerViewState(seconds = 0))
//val state = _state.asStateFlow()


//fun onEvent(event: TimerViewEvent) {
//  when (event) {
//    is TimerViewEvent.ClickedStartTimer -> onClickedStartTimer()
//  is TimerViewEvent.ClickedStopTimer -> onClickedStopTimer()
//}
//}

//private fun onClickedStartTimer() {
//  /** TODO (Tyler):
// * Start incrementing the seconds of the [_state].
//*/
//}

//private fun onClickedStopTimer() {
//  /** TODO (Tyler):
// * Stop incrementing the seconds of the [_state].
//*/
//}


    