package com.majotyler.hiittimer.domain.usecase

import com.majotyler.hiittimer.data.repository.StravaRepository
import com.majotyler.hiittimer.data.repository.StravaTokenStorageRepository

class RefreshStravaTokenUseCase(
    private val stravaRepository: StravaRepository,
    private val stravaTokenStorageRepository: StravaTokenStorageRepository,
) {
    suspend operator fun invoke(refreshToken: String) {
        val result = stravaRepository.refreshAccessToken(
            refreshToken = refreshToken,
        )
        stravaTokenStorageRepository.saveToken(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
            expiresAt = result.expiresAt,
        )
    }
}
