package de.lamaka.fourcastie.city

import de.lamaka.fourcastie.base.ActionResult
import de.lamaka.fourcastie.domain.model.Weather

sealed class CityActionResult : ActionResult {
    object Init : CityActionResult()
    object Loading : CityActionResult()
    data class Loaded(val weather: Weather) : CityActionResult()
    data class FailedToLoad(val message: String) : CityActionResult()
}