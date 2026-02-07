package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.addWorkoutScreen.AddWorkoutDestination
import com.majotyler.hiittimer.presentation.addWorkoutScreen.AddWorkoutViewModel
import kotlin.reflect.KClass

class PlayWorkoutViewModelFactory(
    private val router: (PlayWorkoutDestination) -> Unit,
    private val workouts: List<Workout>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return PlayWorkoutVIewModel(
            router = router,
            workouts = workouts,
        ) as T
    }
}