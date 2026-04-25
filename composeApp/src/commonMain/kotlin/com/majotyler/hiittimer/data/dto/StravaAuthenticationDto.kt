package com.majotyler.hiittimer.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StravaAuthenticationDto(
    @SerialName("token")
    val encryptedToken: String,
)