package com.majotyler.hiittimer.data.repository

import com.majotyler.hiittimer.data.api.StravaApi
import com.majotyler.hiittimer.data.dto.StravaAuthenticationDto
import com.majotyler.hiittimer.network.StravaClientSecret

class StravaRepository(
    private val api: StravaApi
) {
    suspend fun getAccessToken(
        code: String,
    ): StravaAuthenticationDto {
        return api.getAccessToken(
            clientId = StravaClientSecret.STRAVA_CLIENT_ID,
            clientSecret = StravaClientSecret.STRAVA_CLIENT_SECRET,
            code = code,
        )
    }

    suspend fun createActivity(
        accessToken: String,
        name: String,
        type: String,
        startDateLocal: String,
        elapsedTime: Int,
    ) {
        return api.createActivity(accessToken, name, type, startDateLocal, elapsedTime)
    }
}