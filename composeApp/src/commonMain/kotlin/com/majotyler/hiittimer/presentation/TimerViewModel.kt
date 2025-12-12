package com.majotyler.hiittimer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majotyler.hiittimer.presentation.common.navigation.Router
import com.majotyler.hiittimer.presentation.homeScreen.HomeDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Interval(
    val exercises: List<String>,
    val reps: Int
)

class TimerViewModel(
    private val router: Router<TimerDestination>,
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _exercises =
        MutableStateFlow(
            listOf(
                "Lagartijas",
                "gallitos",
                "dominadas",
                " sentadillas",
                "hola",
                "otro"
            )
        )
    val exercises = _exercises.asStateFlow()

    private val _intervals = MutableStateFlow<List<Interval>>(emptyList())
    val intervals = _intervals.asStateFlow()

    private val _enable = MutableStateFlow(false)
    val enable = _enable.asStateFlow()


    private val _reps = MutableStateFlow(0)
    val reps = _reps.asStateFlow()

    fun onEvent(event: TimerViewEvent) {
        when (event) {
            is TimerViewEvent.ClickedAdd -> onClickedAdd()
            is TimerViewEvent.ClickedDelete -> onClickedDelete(event.index)
            is TimerViewEvent.AddReps -> addReps()
            is TimerViewEvent.RemoveReps -> removeReps()
            is TimerViewEvent.AddIntervals -> addIntervals()
            is TimerViewEvent.DeleteIntervals -> deleteIntervals(event.index)
        }
    }

    private fun onClickedAdd() {
        val list = listOf("1", "2", "4", "5")
        _exercises.value = list
        router.routeTo(destination = TimerDestination.NavigateToAddWorkout)
    }

    private fun onClickedDelete(index: Int) {
        _exercises.value = _exercises.value.toMutableList().apply {
            removeAt(index)
        }
    }

    private fun deleteIntervals(index: Int) {
        _intervals.value = _intervals.value.toMutableList().apply {
            removeAt(index)
        }
    }


    private fun addReps() {
        _reps.value++
        validAdd()
    }

    private fun removeReps() {
        if (_reps.value > 0) _reps.value--
        validAdd()
    }

    private fun addIntervals() {
        val newInterval = Interval(_exercises.value, _reps.value)
        _intervals.value = _intervals.value + newInterval
        _exercises.value = emptyList()
        _reps.value = 0
        validAdd()
    }

    private fun validAdd() {
        _enable.value = _reps.value > 0 && _exercises.value.isNotEmpty()
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


    