package com.majotyler.hiittimer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.majotyler.hiittimer.presentation.common.navigation.Router
import com.majotyler.hiittimer.presentation.homeScreen.HomeDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class TimerViewModelFactory(
    private val router: (TimerDestination) -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return TimerViewModel(
            router = router,
        ) as T
    }
}