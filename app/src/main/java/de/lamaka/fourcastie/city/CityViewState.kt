package de.lamaka.fourcastie.city

import de.lamaka.fourcastie.base.ViewState
import de.lamaka.fourcastie.home.WeatherForCity

sealed class CityViewState : ViewState {
    object Init : CityViewState()
    object Loading : CityViewState()
    data class Error(val message: String) : CityViewState()
    data class Loaded(val weather: WeatherForCity) : CityViewState()
}
