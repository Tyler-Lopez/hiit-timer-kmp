package com.majotyler.hiittimer.domain.model

data class Workout(
    val intervals: List<Interval>,
    val name: String,
    val repetitions: Int,
)