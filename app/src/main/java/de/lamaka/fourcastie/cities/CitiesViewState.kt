package de.lamaka.fourcastie.cities

import de.lamaka.fourcastie.base.ViewState

data class CitiesViewState(
    val cities: List<String> = emptyList(),
    val addCity: Boolean = false
) : ViewState