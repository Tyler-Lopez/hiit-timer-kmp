package com.majotyler.hiittimer.data.repository

import com.majotyler.hiittimer.data.model.StoredStravaToken

interface StravaTokenStorageRepository {
    suspend fun getStoredToken(): StoredStravaToken?
    suspend fun saveToken(accessToken: String, refreshToken: String, expiresAt: Long)
}
