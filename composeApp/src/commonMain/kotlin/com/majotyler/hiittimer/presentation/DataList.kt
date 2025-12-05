package com.majotyler.hiittimer.presentation

data class Datos(var nombre: String)

fun Exercises(): List<Datos> {
    return listOf<Datos>(
        Datos("Lagartijas"),
        Datos("gallitos"),
        Datos("dominadas"),
        Datos("Sentadillas"),
        Datos("3x3"),
        Datos("2x5"),
        Datos("plancha"),
        Datos("sentadillas")
    )
}