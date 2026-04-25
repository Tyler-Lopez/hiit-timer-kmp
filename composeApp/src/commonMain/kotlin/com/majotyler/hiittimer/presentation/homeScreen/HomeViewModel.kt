package com.majotyler.hiittimer.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.*
import com.majotyler.hiittimer.data.api.StravaApi
import com.majotyler.hiittimer.data.repository.StravaRepository
import com.majotyler.hiittimer.domain.usecase.CreateStravaActivityUseCase
import com.majotyler.hiittimer.domain.usecase.GetStoredStravaTokenUseCase
import com.majotyler.hiittimer.domain.usecase.RefreshStravaTokenUseCase
import com.majotyler.hiittimer.domain.usecase.SaveStravaTokenUseCase
import com.majotyler.hiittimer.network.HttpClientFactory
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class HomeViewModel(
    private val router: Router<HomeDestination>,
    private val stravaAccessCode: String?,
    private val getStoredStravaTokenUseCase: GetStoredStravaTokenUseCase,
    private val saveStravaTokenUseCase: SaveStravaTokenUseCase,
    private val refreshStravaTokenUseCase: RefreshStravaTokenUseCase,
) : ViewModel() {

    private val _openUrl = MutableSharedFlow<String>()
    val openUrl = _openUrl.asSharedFlow()

    // TODO This is temporary to show something to create a Strava Activity
    val showCreateActivityButton = stravaAccessCode != null

    private val httpClient by lazy {
        HttpClientFactory.create()
    }

    private val stravaApi: StravaApi by lazy {
        StravaApi(httpClient)
    }

    private val stravaRepository by lazy {
        StravaRepository(stravaApi)
    }
    private val createStravaActivityUseCase by lazy {
        CreateStravaActivityUseCase()
    }

    init {
        viewModelScope.launch {
            val storedToken = getStoredStravaTokenUseCase()
            val nowSeconds = Clock.System.now().epochSeconds

            when {
                storedToken != null && storedToken.expiresAt > nowSeconds -> {
                    println("Stored token is valid, expires in ${storedToken.expiresAt - nowSeconds}s")
                    println("access_token: ${storedToken.accessToken}")
                }
                storedToken != null -> {
                    println("Stored token is expired (expired ${nowSeconds - storedToken.expiresAt}s ago), refreshing...")
                    try {
                        refreshStravaTokenUseCase(refreshToken = storedToken.refreshToken)
                        println("Token refresh successful")
                    } catch (e: Exception) {
                        println("Token refresh error: $e")
                    }
                }
                else -> {
                    println("No stored token found")
                }
            }

            if (stravaAccessCode != null) {
                try {
                    println("Exchanging OAuth code for token via proxy...")
                    val result = stravaRepository.getAccessToken(code = stravaAccessCode)
                    println("access_token: ${result.accessToken}")
                    println("refresh_token: ${result.refreshToken}")
                    println("token_type: ${result.tokenType}")
                    println("expires_at: ${result.expiresAt}")
                    println("expires_in: ${result.expiresIn}")
                    saveStravaTokenUseCase(
                        accessToken = result.accessToken,
                        refreshToken = result.refreshToken,
                        expiresAt = result.expiresAt,
                    )
                } catch (e: Exception) {
                    println("Token exchange error: $e")
                }
            }
        }
    }

    fun onEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.ClickedConnectWithStrava -> onClickedConnectWithStrava()
            is HomeViewEvent.ClickedCreateStravaActivity -> onClickedCreateStravaActivity()
            is HomeViewEvent.ClickedLaunchBuildWorkouts -> onClickedLaunchBuildWorkouts()
        }
    }

    private fun onClickedConnectWithStrava() {
        viewModelScope.launch {
            _openUrl.emit(value = getAuthUri())
        }
    }

    private fun onClickedCreateStravaActivity() {
        viewModelScope.launch {
            createStravaActivityUseCase()
        }
    }

    private fun onClickedLaunchBuildWorkouts() {
        router.routeTo(destination = HomeDestination.NavigateToBuildWorkouts)
    }

    private fun getAuthUri() = URLBuilder(STRAVA_BASE_AUTH_URL).apply {
        parameters.append(STRAVA_QUERY_CLIENT_ID, STRAVA_CLIENT_ID_STRING)
        parameters.append(STRAVA_QUERY_REDIRECT_URI, STRAVA_REDIRECT_URI)
        parameters.append(STRAVA_QUERY_RESPONSE_TYPE, STRAVA_RESPONSE_TYPE)
        parameters.append(STRAVA_QUERY_APPROVAL_PROMPT, STRAVA_APPROVAL_PROMPT)
        parameters.append(STRAVA_QUERY_SCOPE, STRAVA_SCOPE)
    }.buildString()

    private companion object {
        const val STRAVA_BASE_AUTH_URL = "https://www.strava.com/oauth/mobile/authorize"

        const val STRAVA_QUERY_CLIENT_ID = "client_id"
        const val STRAVA_QUERY_REDIRECT_URI = "redirect_uri"
        const val STRAVA_QUERY_RESPONSE_TYPE = "response_type"
        const val STRAVA_QUERY_APPROVAL_PROMPT = "approval_prompt"
        const val STRAVA_QUERY_SCOPE = "scope"

        const val STRAVA_CLIENT_ID = 208269
        const val STRAVA_CLIENT_ID_STRING = STRAVA_CLIENT_ID.toString()
        const val STRAVA_REDIRECT_URI = "com.majotyler.hiittimer://myapp.com"
        const val STRAVA_RESPONSE_TYPE = "code"
        const val STRAVA_APPROVAL_PROMPT = "auto"
        const val STRAVA_SCOPE = "activity:read,activity:read_all,activity:write"
    }
}
