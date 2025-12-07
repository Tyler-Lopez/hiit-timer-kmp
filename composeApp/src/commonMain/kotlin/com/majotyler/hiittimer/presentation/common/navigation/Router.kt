package com.majotyler.hiittimer.presentation.common.navigation

/**
 * A [Router] abstracts requests to route to a given [Destination]. It is expected that a child
 * class of [Destination] is used instead of directly using [Destination].
 */
fun interface Router<Destination> {
    fun routeTo(destination: Destination)
}