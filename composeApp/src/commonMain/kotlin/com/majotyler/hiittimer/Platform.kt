package com.majotyler.hiittimer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform