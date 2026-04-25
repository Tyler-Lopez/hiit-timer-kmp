package com.majotyler.hiittimer.presentation.workoutReviewScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majotyler.hiittimer.data.repository.StravaRepository
import com.majotyler.hiittimer.domain.usecase.CreateStravaActivityUseCase
import com.majotyler.hiittimer.domain.usecase.GetStoredStravaTokenUseCase
import com.majotyler.hiittimer.domain.usecase.RefreshStravaTokenUseCase
import com.majotyler.hiittimer.domain.usecase.SaveStravaTokenUseCase
import com.majotyler.hiittimer.presentation.common.navigation.Router
import io.ktor.http.URLBuilder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class WorkoutReviewViewModel(
    private val router: Router<WorkoutReviewDestination>,
    private val stravaAccessCode: String?,
    private val name: String,
    private val description: String,
    private val startDateLocal: String,
    private val elapsedTime: Int,
    private val getStoredStravaTokenUseCase: GetStoredStravaTokenUseCase,
    private val refreshStravaTokenUseCase: RefreshStravaTokenUseCase,
    private val saveStravaTokenUseCase: SaveStravaTokenUseCase,
    private val createStravaActivityUseCase: CreateStravaActivityUseCase,
    private val stravaRepository: StravaRepository,
) : ViewModel() {

    private val _openUrl = MutableSharedFlow<String>()
    val openUrl = _openUrl.asSharedFlow()

    private val _showCreateActivityButton = MutableStateFlow(false)
    val showCreateActivityButton = _showCreateActivityButton.asStateFlow()

    init {
        viewModelScope.launch {
            val storedToken = getStoredStravaTokenUseCase()
            val nowSeconds = Clock.System.now().epochSeconds

            when {
                storedToken != null && storedToken.expiresAt > nowSeconds -> {
                    _showCreateActivityButton.value = true
                }
                storedToken != null -> {
                    try {
                        refreshStravaTokenUseCase(refreshToken = storedToken.refreshToken)
                        _showCreateActivityButton.value = true
                    } catch (e: Exception) {
                        println("Token refresh error: $e")
                    }
                }
                else -> Unit
            }

            if (stravaAccessCode != null) {
                try {
                    val result = stravaRepository.getAccessToken(code = stravaAccessCode)
                    saveStravaTokenUseCase(
                        accessToken = result.accessToken,
                        refreshToken = result.refreshToken,
                        expiresAt = result.expiresAt,
                    )
                    _showCreateActivityButton.value = true
                } catch (e: Exception) {
                    println("Token exchange error: $e")
                }
            }
        }
    }

    fun onEvent(event: WorkoutReviewViewEvent) {
        when (event) {
            is WorkoutReviewViewEvent.ClickedConnectWithStrava -> onClickedConnectWithStrava()
            is WorkoutReviewViewEvent.ClickedCreateStravaActivity -> onClickedCreateStravaActivity()
            is WorkoutReviewViewEvent.ClickedNavigateUp -> router.routeTo(WorkoutReviewDestination.NavigateUp)
        }
    }

    private fun onClickedConnectWithStrava() {
        viewModelScope.launch {
            _openUrl.emit(value = getAuthUri())
        }
    }

    private fun onClickedCreateStravaActivity() {
        viewModelScope.launch {
            try {
                createStravaActivityUseCase(
                    name = name,
                    description = description,
                    sportType = "HighIntensityIntervalTraining",
                    startDateLocal = startDateLocal,
                    elapsedTime = elapsedTime,
                )
                println("Create activity successful")
            } catch (e: Exception) {
                println("Create activity error: $e")
            }
        }
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
