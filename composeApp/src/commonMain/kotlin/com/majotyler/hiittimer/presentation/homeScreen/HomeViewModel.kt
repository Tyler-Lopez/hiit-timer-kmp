package com.majotyler.hiittimer.presentation.homeScreen

import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.lifecycle.ViewModel
import io.ktor.http.*
import androidx.lifecycle.viewModelScope
import com.majotyler.hiittimer.presentation.common.navigation.Router
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val router: Router<HomeDestination>,
) : ViewModel() {

    private val _openUrl = MutableSharedFlow<String>()
    val openUrl = _openUrl.asSharedFlow()

    fun onEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.ClickedConnectWithStrava -> onClickedConnectWithStrava()
            is HomeViewEvent.ClickedLaunchBuildWorkouts -> onClickedLaunchBuildWorkouts()
        }
    }

    private fun onClickedConnectWithStrava() {
        viewModelScope.launch {
            _openUrl.emit(value = getAuthUri())
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