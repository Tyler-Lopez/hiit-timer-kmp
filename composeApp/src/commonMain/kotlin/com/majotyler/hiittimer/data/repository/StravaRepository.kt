package com.majotyler.hiittimer.data.repository

import com.majotyler.hiittimer.data.api.StravaApi
import com.majotyler.hiittimer.data.dto.StravaAuthenticationDto

class StravaRepository(
    private val api: StravaApi
) {
    suspend fun getAccessToken(
        code: String,
    ): StravaAuthenticationDto {
        return api.getAccessToken(code = code)
    }

    suspend fun refreshAccessToken(
        refreshToken: String,
    ): StravaAuthenticationDto {
        return api.refreshAccessToken(refreshToken = refreshToken)
    }

    suspend fun createActivity(
        accessToken: String,
        name: String,
        sportType: String,
        startDateLocal: String,
        elapsedTime: Int,
    ) {
        api.createActivity(
            accessToken = accessToken,
            name = name,
            sportType = sportType,
            startDateLocal = startDateLocal,
            elapsedTime = elapsedTime,
        )
    }
}