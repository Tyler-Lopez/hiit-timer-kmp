package com.majotyler.hiittimer.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.majotyler.hiittimer.data.api.StravaApi
import com.majotyler.hiittimer.data.repository.StravaRepository
import com.majotyler.hiittimer.data.repository.StravaTokenStorageRepository
import com.majotyler.hiittimer.domain.usecase.CreateStravaActivityUseCase
import com.majotyler.hiittimer.domain.usecase.GetStoredStravaTokenUseCase
import com.majotyler.hiittimer.domain.usecase.RefreshStravaTokenUseCase
import com.majotyler.hiittimer.domain.usecase.SaveStravaTokenUseCase
import com.majotyler.hiittimer.network.HttpClientFactory
import kotlin.reflect.KClass

class HomeViewModelFactory(
    private val router: (HomeDestination) -> Unit,
    private val stravaAccessCode: String?,
    private val stravaTokenStorageRepository: StravaTokenStorageRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        val stravaRepository = StravaRepository(api = StravaApi(client = HttpClientFactory.create()))
        return HomeViewModel(
            router = router,
            stravaAccessCode = stravaAccessCode,
            stravaRepository = stravaRepository,
            getStoredStravaTokenUseCase = GetStoredStravaTokenUseCase(repository = stravaTokenStorageRepository),
            saveStravaTokenUseCase = SaveStravaTokenUseCase(repository = stravaTokenStorageRepository),
            refreshStravaTokenUseCase = RefreshStravaTokenUseCase(
                stravaRepository = stravaRepository,
                stravaTokenStorageRepository = stravaTokenStorageRepository,
            ),
            createStravaActivityUseCase = CreateStravaActivityUseCase(
                stravaRepository = stravaRepository,
                stravaTokenStorageRepository = stravaTokenStorageRepository,
            ),
        ) as T
    }
}
