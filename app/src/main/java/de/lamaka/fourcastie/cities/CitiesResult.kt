package de.lamaka.fourcastie.cities

import de.lamaka.fourcastie.base.Result

sealed class CitiesResult : Result {
    data class Loaded(val cities: List<String>) : CitiesResult()
    object AddCity : CitiesResult()
}