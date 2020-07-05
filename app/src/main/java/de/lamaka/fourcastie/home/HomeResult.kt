package de.lamaka.fourcastie.home

import de.lamaka.fourcastie.base.Result
import de.lamaka.fourcastie.domain.model.Weather

sealed class HomeResult : Result {
    object Loading : HomeResult()
    data class WeatherLoaded(val weather: Weather) : HomeResult()
}