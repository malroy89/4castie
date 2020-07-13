package de.lamaka.fourcastie.city

import de.lamaka.fourcastie.base.ViewState

sealed class CityViewState : ViewState {
    object Init : CityViewState()
    object Loading : CityViewState()
    data class Error(val message: String) : CityViewState()
    data class Loaded(val weatherView: WeatherView) : CityViewState()
}
//data class CityViewState(
//    val loading: Boolean = false,
//    val weather: WeatherView? = null,
//    val error: String? = null
//) : ViewState {
//    fun isEmpty() = !loading && weather == null && error == null
//}

