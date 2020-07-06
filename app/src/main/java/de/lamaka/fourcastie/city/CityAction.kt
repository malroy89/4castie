package de.lamaka.fourcastie.city

import de.lamaka.fourcastie.base.Action

sealed class CityAction : Action {
    object Init : CityAction()
    data class Load(val cityName: String?) : CityAction()
}