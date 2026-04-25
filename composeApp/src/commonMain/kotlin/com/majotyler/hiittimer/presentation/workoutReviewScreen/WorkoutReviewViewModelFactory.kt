package com.majotyler.hiittimer.presentation.workoutReviewScreen

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

class WorkoutReviewViewModelFactory(
    private val router: (WorkoutReviewDestination) -> Unit,
    private val stravaAccessCode: String?,
    private val stravaTokenStorageRepository: StravaTokenStorageRepository,
    private val name: String,
    private val description: String,
    private val startDateLocal: String,
    private val elapsedTime: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        val stravaRepository = StravaRepository(api = StravaApi(client = HttpClientFactory.create()))
        return WorkoutReviewViewModel(
            router = router,
            stravaAccessCode = stravaAccessCode,
            name = name,
            description = description,
            startDateLocal = startDateLocal,
            elapsedTime = elapsedTime,
            getStoredStravaTokenUseCase = GetStoredStravaTokenUseCase(repository = stravaTokenStorageRepository),
            refreshStravaTokenUseCase = RefreshStravaTokenUseCase(
                stravaRepository = stravaRepository,
                stravaTokenStorageRepository = stravaTokenStorageRepository,
            ),
            saveStravaTokenUseCase = SaveStravaTokenUseCase(repository = stravaTokenStorageRepository),
            createStravaActivityUseCase = CreateStravaActivityUseCase(
                stravaRepository = stravaRepository,
                stravaTokenStorageRepository = stravaTokenStorageRepository,
            ),
            stravaRepository = stravaRepository,
        ) as T
    }
}
