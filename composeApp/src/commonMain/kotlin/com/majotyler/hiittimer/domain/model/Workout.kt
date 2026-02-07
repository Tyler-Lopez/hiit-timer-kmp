package com.majotyler.hiittimer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val intervals: List<Interval>,
    val name: String,
    val repetitions: Int,
)