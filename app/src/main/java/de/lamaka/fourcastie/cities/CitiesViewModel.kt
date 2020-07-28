package de.lamaka.fourcastie.cities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import de.lamaka.fourcastie.base.BaseViewModel
import de.lamaka.fourcastie.domain.CityRepository

class CitiesViewModel @ViewModelInject constructor(
    private val cityRepository: CityRepository,
    cityReducer: CitiesReducer
) : BaseViewModel<CitiesAction, CitiesViewState, CitiesActionResult>(CitiesAction.Load, cityReducer) {

    override fun perform(action: CitiesAction) = liveData {
        when (action) {
            CitiesAction.Load -> {
                val cities = cityRepository.getAllCities()
                emit(CitiesActionResult.Loaded(cities))
            }
            CitiesAction.AddCity -> emit(CitiesActionResult.AddCity)
            is CitiesAction.FinishAdding -> {
                cityRepository.saveCity(action.cityName)
                val cities = cityRepository.getAllCities()
                emit(CitiesActionResult.Loaded(cities))
            }
            CitiesAction.AddCityCancel -> emit(CitiesActionResult.AddCityCancelled)
        }
    }

    override var currentState: CitiesViewState = CitiesViewState()

}