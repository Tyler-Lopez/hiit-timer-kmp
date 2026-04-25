package com.majotyler.hiittimer.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.majotyler.hiittimer.data.repository.StravaTokenStorageRepository
import com.majotyler.hiittimer.domain.usecase.GetStoredStravaTokenUseCase
import com.majotyler.hiittimer.domain.usecase.SaveStravaTokenUseCase
import kotlin.reflect.KClass

class HomeViewModelFactory(
    private val router: (HomeDestination) -> Unit,
    private val stravaAccessCode: String?,
    private val stravaTokenStorageRepository: StravaTokenStorageRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return HomeViewModel(
            router = router,
            stravaAccessCode = stravaAccessCode,
            getStoredStravaTokenUseCase = GetStoredStravaTokenUseCase(repository = stravaTokenStorageRepository),
            saveStravaTokenUseCase = SaveStravaTokenUseCase(repository = stravaTokenStorageRepository),
        ) as T
    }
}
