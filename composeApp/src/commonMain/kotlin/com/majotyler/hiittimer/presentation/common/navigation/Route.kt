package com.majotyler.hiittimer.presentation.common.navigation

import androidx.navigation3.runtime.NavKey
import com.majotyler.hiittimer.domain.model.Workout
import kotlinx.serialization.Serializable

/**
 * Every screen is represented by a [Route].
 *
 * When a new screen is created, it should be added as a child of [Route]. It also must be added
 * as an entry in [NavigationRoot].
 */
@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object AddWorkout : Route

    @Serializable
    data object BuildWorkouts : Route

    @Serializable
    data object Home : Route

    @Serializable
    data class PlayWorkout(
        val workouts: List<Workout>,
    ) : Route
}