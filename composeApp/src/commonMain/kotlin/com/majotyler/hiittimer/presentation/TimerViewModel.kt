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

    private val _exercises = MutableStateFlow(listOf("Lagartijas","gallitos", "dominadas"," sentadillas"))
    val exercises = _exercises.asStateFlow()

    fun onEvent(event: TimerViewEvent) {
        when (event) {
            is TimerViewEvent.ClickAdd -> {
                viewModelScope.launch {
                    ClickAdd()
                }}
            is TimerViewEvent.onDelete -> {
                onDelete(event.item)
                }

    }
    }

    suspend fun ClickAdd() {
        _loading.value = true
        delay(4000)
        _loading.value = false


    }
    fun onDelete(item: String){
        _exercises.value = _exercises.value.filter { it != item }
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


    