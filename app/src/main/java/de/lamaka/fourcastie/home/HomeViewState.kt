package de.lamaka.fourcastie.home

import de.lamaka.fourcastie.base.ViewState
import de.lamaka.fourcastie.city.WeatherView

data class HomeViewState(
    val loading: Boolean = false,
    val weather: WeatherView? = null,
    val error: String? = null,
    val missingPermissions: List<String> = emptyList()
) : ViewState