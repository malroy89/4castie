package de.lamaka.fourcastie.home

import de.lamaka.fourcastie.base.ViewState
import de.lamaka.fourcastie.domain.model.Weather

data class HomeViewState(
    val loading: Boolean = true,
    val weather: Weather? = null
) : ViewState