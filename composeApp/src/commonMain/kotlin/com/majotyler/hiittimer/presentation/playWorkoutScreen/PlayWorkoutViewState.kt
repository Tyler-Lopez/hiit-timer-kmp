package com.majotyler.hiittimer.presentation.playWorkoutScreen

data class PlayWorkoutViewState(
    val confirmationDialogVisible: Boolean,
    val intervalName: String,
    val intervalNumber: Int,
    val intervalTotal: Int,
    val isResting: Boolean,
    val progressDisplay: String,
    val progress: Float,
)
