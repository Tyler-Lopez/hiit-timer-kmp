package com.majotyler.hiittimer.presentation.createWorkoutScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class CreateWorkoutViewModelFactory(
    private val router: (CreateWorkoutDestination) -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return CreateWorkoutViewModel(
            router = router,
        ) as T
    }
}
