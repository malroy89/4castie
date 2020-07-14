package de.lamaka.fourcastie.city

import de.lamaka.fourcastie.base.ViewState
import de.lamaka.fourcastie.home.WeatherForCity
import de.lamaka.fourcastie.ui.model.WeatherView

sealed class CityViewState : ViewState {
    object Init : CityViewState()
    object Loading : CityViewState()
    data class Error(val message: String) : CityViewState()
    data class Loaded(val weather: WeatherForCity) : CityViewState()
}
//data class CityViewState(
//    val loading: Boolean = false,
//    val weather: WeatherView? = null,
//    val error: String? = null
//) : ViewState {
//    fun isEmpty() = !loading && weather == null && error == null
//}

