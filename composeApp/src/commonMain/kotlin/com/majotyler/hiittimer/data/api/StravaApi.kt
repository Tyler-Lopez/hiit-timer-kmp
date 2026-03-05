package com.majotyler.hiittimer.data.api

import com.majotyler.hiittimer.data.dto.StravaAuthenticationDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters

class StravaApi(private val client: HttpClient) {
    suspend fun getAccessToken(
        clientId: Int,
        clientSecret: String,
        code: String,
    ): StravaAuthenticationDto {
        return client.post(urlString = StravaEndpoints.OAUTH_TOKEN) {
            parameter(key = "client_id", value = clientId)
            parameter(key = "client_secret", value = clientSecret)
            parameter(key = "code", value = code)
            parameter(key = "grant_type", value = StravaGrantTypes.AUTH_CODE)
        }.body()
    }

    suspend fun createActivity(
        accessToken: String,
        name: String,
        type: String,
        startDateLocal: String,
        elapsedTime: Int,
    ) {
        return client.post(urlString = StravaEndpoints.ACTIVITIES) {
            header(key = HttpHeaders.Authorization, value = "Bearer $accessToken")
            setBody(
                body = FormDataContent(
                    formData = Parameters.build {
                        append(name = "name", value = name)
                        append(name = "sport_type", value = type)
                        append(name = "start_date_local", value = startDateLocal)
                        append(name = "elapsed_time", value = elapsedTime.toString())
                    }
                )
            )
        }.body()
    }
}

private object StravaEndpoints {
    const val BASE_URL = "https://www.strava.com"
    const val OAUTH_TOKEN = "$BASE_URL/oauth/token"
    const val ACTIVITIES = "$BASE_URL/api/v3/activities"
}

private object StravaGrantTypes {
    const val AUTH_CODE = "authorization_code"
}