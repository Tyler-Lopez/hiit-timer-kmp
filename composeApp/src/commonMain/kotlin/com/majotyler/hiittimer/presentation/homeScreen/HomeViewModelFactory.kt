package com.majotyler.hiittimer.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.majotyler.hiittimer.presentation.TimerDestination
import com.majotyler.hiittimer.presentation.TimerViewModel
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlin.reflect.KClass

class HomeViewModelFactory(
    private val router: (HomeDestination) -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return HomeViewModel(
            router = router,
        ) as T
    }
}