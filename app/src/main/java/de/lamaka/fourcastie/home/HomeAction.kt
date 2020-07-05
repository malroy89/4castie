package de.lamaka.fourcastie.home

import de.lamaka.fourcastie.base.Action

sealed class HomeAction : Action {
    object LoadWeatherForLocation : HomeAction()
    data class LoadWeatherForCity(val cityName: String) : HomeAction()
}