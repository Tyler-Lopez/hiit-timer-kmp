package com.majotyler.hiittimer.data.api

import com.majotyler.hiittimer.data.dto.StravaAuthenticationDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType

class StravaApi(private val client: HttpClient) {
    suspend fun getAccessTokenViaProxy(
        code: String,
    ): StravaAuthenticationDto {
        val response = client.post(urlString = ProxyEndpoints.OAUTH_TOKEN) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("code" to code))
        }
        println("Proxy token exchange status: ${response.status}")
        return response.body()
    }

    suspend fun refreshAccessTokenViaProxy(
        refreshToken: String,
    ): StravaAuthenticationDto {
        val response = client.post(urlString = ProxyEndpoints.OAUTH_REFRESH) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("refresh_token" to refreshToken))
        }
        println("Proxy token refresh status: ${response.status}")
        return response.body()
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
    const val ACTIVITIES = "$BASE_URL/api/v3/activities"
}

private object ProxyEndpoints {
    const val BASE_URL = "https://strava-token-proxy.hiit-timer-app.workers.dev"
    const val OAUTH_TOKEN = BASE_URL
    const val OAUTH_REFRESH = "$BASE_URL/refresh"
}