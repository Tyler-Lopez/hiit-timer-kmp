package com.majotyler.hiittimer.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class HomeViewModelFactory(
    private val router: (HomeDestination) -> Unit,
    private val stravaAccessToken: String?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return HomeViewModel(
            router = router,
            stravaAccessCode = stravaAccessToken,
        ) as T
    }
}