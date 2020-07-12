package de.lamaka.fourcastie.home

import de.lamaka.fourcastie.base.ActionResult
import de.lamaka.fourcastie.domain.location.UserLocation
import de.lamaka.fourcastie.domain.model.Weather

sealed class HomeActionResult : ActionResult {
    object Loading : HomeActionResult()
    data class MissingPermission(val permissions: List<String>) : HomeActionResult()
    data class WeatherLoaded(val weather: Weather) : HomeActionResult()
    data class FailedToLoadWeather(val message: String) : HomeActionResult()
}