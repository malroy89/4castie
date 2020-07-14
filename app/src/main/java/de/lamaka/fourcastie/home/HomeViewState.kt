package de.lamaka.fourcastie.home

import de.lamaka.fourcastie.base.ViewState
import de.lamaka.fourcastie.ui.model.ForecastView
import de.lamaka.fourcastie.ui.model.WeatherView

data class HomeViewState(
    val loading: Boolean = false,
    val weather: WeatherForCity? = null,
    val error: String? = null,
    val missingPermissions: List<String> = emptyList()
) : ViewState

data class WeatherForCity(
    val weather: WeatherView,
    val forecast: List<ForecastView>
)