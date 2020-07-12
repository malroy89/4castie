package de.lamaka.fourcastie.home

import de.lamaka.fourcastie.base.Action

sealed class HomeAction : Action {
    object LoadWeatherForCurrentPosition : HomeAction()
    data class PermissionsResolved(val allPermissionsGiven: Boolean) : HomeAction()
}