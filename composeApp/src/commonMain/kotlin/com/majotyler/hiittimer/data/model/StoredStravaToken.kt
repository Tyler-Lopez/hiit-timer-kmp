package com.majotyler.hiittimer.data.model

data class StoredStravaToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long,
)
