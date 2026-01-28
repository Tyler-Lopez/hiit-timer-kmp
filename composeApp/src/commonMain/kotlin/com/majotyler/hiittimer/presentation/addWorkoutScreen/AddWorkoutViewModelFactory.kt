package com.majotyler.hiittimer.presentation.addWorkoutScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class AddWorkoutViewModelFactory(
    private val router: (AddWorkoutDestination) -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return AddWorkoutViewModel(
            router = router,
        ) as T
    }
}