package de.lamaka.fourcastie.cities

import de.lamaka.fourcastie.base.Reducer
import javax.inject.Inject

class CitiesReducer @Inject constructor() : Reducer<CitiesViewState, CitiesActionResult> {

    override fun reduce(
        currentState: CitiesViewState,
        actionResult: CitiesActionResult
    ): CitiesViewState {
        return when (actionResult) {
            is CitiesActionResult.Loaded -> currentState.copy(
                addCity = false,
                cities = actionResult.cities
            )
            CitiesActionResult.AddCity -> currentState.copy(addCity = true)
            CitiesActionResult.AddCityCancelled -> currentState.copy(addCity = false)
        }
    }
}