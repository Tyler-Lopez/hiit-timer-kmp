package com.majotyler.hiittimer.presentation.workoutReviewScreen

import com.majotyler.hiittimer.presentation.common.navigation.Destination

sealed interface WorkoutReviewDestination : Destination {
    data object NavigateUp : WorkoutReviewDestination
}
