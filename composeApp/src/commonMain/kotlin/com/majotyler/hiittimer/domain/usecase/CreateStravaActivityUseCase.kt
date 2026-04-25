package com.majotyler.hiittimer.domain.usecase

import com.majotyler.hiittimer.data.repository.StravaRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

class CreateStravaActivityUseCase(
    private val stravaAccessCode: String,
    private val repository: StravaRepository,
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke() {
        val accessToken = repository.getAccessToken(code = stravaAccessCode)

        repository.createActivity(
            accessToken = accessToken.accessToken,
            name = "Created from HIIT App",
            type = "HighIntensityIntervalTraining",
            startDateLocal = TimeZone.currentSystemDefault()
                .let { tz ->
                    Clock.System.now()
                        .minus(1.hours)
                        .toLocalDateTime(tz)
                        .toString()
                },
            elapsedTime = 60,
        )
    }
}