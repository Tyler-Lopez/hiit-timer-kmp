package com.majotyler.hiittimer.domain.model

data class Interval(
    val duration: Int,
    val name: String,
    val rest: Int,
)