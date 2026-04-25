package com.majotyler.hiittimer.data.api

import com.majotyler.hiittimer.data.dto.StravaAuthenticationDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class StravaApi(private val client: HttpClient) {
    suspend fun getAccessToken(
        code: String,
    ): StravaAuthenticationDto {
        val response = client.post(urlString = Endpoints.OAUTH_TOKEN) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("code" to code))
        }
        println("Token exchange status: ${response.status}")
        return response.body()
    }

    suspend fun refreshAccessToken(
        refreshToken: String,
    ): StravaAuthenticationDto {
        val response = client.post(urlString = Endpoints.OAUTH_REFRESH) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("refresh_token" to refreshToken))
        }
        println("Token refresh status: ${response.status}")
        return response.body()
    }

    suspend fun createActivity(
        accessToken: String,
        name: String,
        sportType: String,
        startDateLocal: String,
        elapsedTime: Int,
    ) {
        val response = client.post(urlString = Endpoints.CREATE_ACTIVITY) {
            contentType(ContentType.Application.Json)
            setBody(CreateActivityRequest(
                accessToken = accessToken,
                name = name,
                sportType = sportType,
                startDateLocal = startDateLocal,
                elapsedTime = elapsedTime,
            ))
        }
        println("Create activity status: ${response.status}")
    }
}

@Serializable
private data class CreateActivityRequest(
    @SerialName("access_token") val accessToken: String,
    @SerialName("name") val name: String,
    @SerialName("sport_type") val sportType: String,
    @SerialName("start_date_local") val startDateLocal: String,
    @SerialName("elapsed_time") val elapsedTime: Int,
)

private object Endpoints {
    const val BASE_URL = "https://strava-token-proxy.hiit-timer-app.workers.dev"
    const val OAUTH_TOKEN = BASE_URL
    const val OAUTH_REFRESH = "$BASE_URL/refresh"
    const val CREATE_ACTIVITY = "$BASE_URL/activity"
}
