package de.lamaka.fourcastie.city

import de.lamaka.fourcastie.base.ViewState

data class CityViewState(
    val loading: Boolean = false,
    val weather: WeatherView? = null,
    val error: String? = null
) : ViewState {
    fun isEmpty() = !loading && weather == null && error == null
}

