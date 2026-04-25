package com.majotyler.hiittimer.domain.usecase

import com.majotyler.hiittimer.data.repository.StravaRepository
import com.majotyler.hiittimer.data.repository.StravaTokenStorageRepository

class CreateStravaActivityUseCase(
    private val stravaRepository: StravaRepository,
    private val stravaTokenStorageRepository: StravaTokenStorageRepository,
) {
    suspend operator fun invoke(
        name: String,
        sportType: String,
        startDateLocal: String,
        elapsedTime: Int,
    ) {
        val token = stravaTokenStorageRepository.getStoredToken()
            ?: error("No stored Strava token")
        stravaRepository.createActivity(
            accessToken = token.accessToken,
            name = name,
            sportType = sportType,
            startDateLocal = startDateLocal,
            elapsedTime = elapsedTime,
        )
    }
}
