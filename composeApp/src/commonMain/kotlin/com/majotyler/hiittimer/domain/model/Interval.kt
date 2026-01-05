package com.majotyler.hiittimer.domain.model

data class Interval(
    val workouts: List<Workout>,
    val repetitions: Int,
)