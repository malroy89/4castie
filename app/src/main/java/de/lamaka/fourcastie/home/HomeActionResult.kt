package de.lamaka.fourcastie.home

import de.lamaka.fourcastie.base.ActionResult
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather

sealed class HomeActionResult : ActionResult {
    object Loading : HomeActionResult()
    data class MissingPermission(val permissions: List<String>) : HomeActionResult()
    data class WeatherLoaded(val weather: Weather, val forecast: List<Forecast>) : HomeActionResult()
    data class FailedToLoadWeather(val message: String) : HomeActionResult()
}