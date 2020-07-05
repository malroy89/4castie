package de.lamaka.fourcastie.cities

import de.lamaka.fourcastie.base.Action

sealed class CitiesAction : Action {
    object Load : CitiesAction()
    object AddCity : CitiesAction()
    data class FinishAdding(val cityName: String) : CitiesAction()
}