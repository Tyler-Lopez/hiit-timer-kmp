package com.majotyler.hiittimer.data.repository

import com.majotyler.hiittimer.data.api.StravaApi
import com.majotyler.hiittimer.data.dto.StravaAuthenticationDto

class StravaRepository(
    private val api: StravaApi
) {
    suspend fun getAccessToken(
        code: String,
    ): StravaAuthenticationDto {
        return api.getAccessTokenViaProxy(code = code)
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