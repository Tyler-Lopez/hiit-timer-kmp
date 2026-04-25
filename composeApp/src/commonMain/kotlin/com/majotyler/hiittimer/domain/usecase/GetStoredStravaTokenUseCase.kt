package com.majotyler.hiittimer.domain.usecase

import com.majotyler.hiittimer.data.model.StoredStravaToken
import com.majotyler.hiittimer.data.repository.StravaTokenStorageRepository

class GetStoredStravaTokenUseCase(
    private val repository: StravaTokenStorageRepository,
) {
    suspend operator fun invoke(): StoredStravaToken? = repository.getStoredToken()
}
