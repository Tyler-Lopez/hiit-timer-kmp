package com.majotyler.hiittimer.presentation.buildWorkouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class BuildWorkoutsViewModelFactory(
    private val router: (BuildWorkoutsDestination) -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return BuildWorkoutsViewModel(
            router = router,
        ) as T
    }
}