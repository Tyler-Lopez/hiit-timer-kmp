package com.majotyler.hiittimer.presentation.workoutReviewScreen

sealed interface WorkoutReviewViewEvent {
    data object ClickedConnectWithStrava : WorkoutReviewViewEvent
    data object ClickedCreateStravaActivity : WorkoutReviewViewEvent
    data object ClickedNavigateUp : WorkoutReviewViewEvent
}
