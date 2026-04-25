package com.majotyler.hiittimer.domain.usecase

import com.majotyler.hiittimer.data.repository.StravaTokenStorageRepository

class SaveStravaTokenUseCase(
    private val repository: StravaTokenStorageRepository,
) {
    suspend operator fun invoke(accessToken: String, refreshToken: String, expiresAt: Long) {
        repository.saveToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresAt = expiresAt,
        )
    }
}
