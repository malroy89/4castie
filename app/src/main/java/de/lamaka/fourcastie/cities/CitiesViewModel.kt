package de.lamaka.fourcastie.cities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import de.lamaka.fourcastie.base.BaseViewModel
import de.lamaka.fourcastie.domain.CityRepository

class CitiesViewModel @ViewModelInject constructor(
    private val cityRepository: CityRepository
) : BaseViewModel<CitiesAction, CitiesViewState, CitiesResult>(CitiesAction.Load) {

    override fun perform(action: CitiesAction) = liveData {
        when (action) {
            CitiesAction.Load -> {
                val cities = cityRepository.getAllCities()
                emit(CitiesResult.Loaded(cities))
            }
            CitiesAction.AddCity -> emit(CitiesResult.AddCity)
            is CitiesAction.FinishAdding -> {
                cityRepository.saveCity(action.cityName)
                val cities = cityRepository.getAllCities()
                emit(CitiesResult.Loaded(cities))
            }
            CitiesAction.AddCityCancel -> emit(CitiesResult.AddCityCancelled)
        }
    }

    override var currentState: CitiesViewState = CitiesViewState()

    override fun reduce(result: CitiesResult): CitiesViewState {
        return when (result) {
            is CitiesResult.Loaded -> currentState.copy(addCity = false, cities = result.cities)
            CitiesResult.AddCity -> currentState.copy(addCity = true)
            CitiesResult.AddCityCancelled -> currentState.copy(addCity = false)
        }
    }

}