package com.majotyler.hiittimer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Interval(
    val duration: Int,
    val name: String,
    val rest: Int,
)