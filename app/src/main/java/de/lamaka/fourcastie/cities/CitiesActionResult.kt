package de.lamaka.fourcastie.cities

import de.lamaka.fourcastie.base.ActionResult

sealed class CitiesActionResult : ActionResult {
    data class Loaded(val cities: List<String>) : CitiesActionResult()
    object AddCity : CitiesActionResult()
    object AddCityCancelled : CitiesActionResult()
}