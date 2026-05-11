package com.majotyler.hiittimer.presentation.chooseWorkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class ChooseWorkoutViewModelFactory(
    private val router: (ChooseWorkoutDestination) -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return ChooseWorkoutViewModel(
            router = router,
        ) as T
    }
}
