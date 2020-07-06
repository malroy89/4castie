package de.lamaka.fourcastie.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import de.lamaka.fourcastie.base.BaseViewModel
import de.lamaka.fourcastie.domain.WeatherRepository

class HomeViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository
) : BaseViewModel<HomeAction, HomeViewState, HomeActionResult>(HomeAction.LoadWeatherForLocation) {

    override fun perform(action: HomeAction) = liveData {
        when (action) {
            is HomeAction.LoadWeatherForCity -> {
                emit(HomeActionResult.Loading)
                emit(HomeActionResult.WeatherLoaded(weatherRepository.loadForCity(action.cityName)))
            }
        }
    }

    override var currentState: HomeViewState = HomeViewState()

    override fun reduce(result: HomeActionResult): HomeViewState {
        return when (result) {
            HomeActionResult.Loading -> currentState.copy(loading = true)
            is HomeActionResult.WeatherLoaded -> currentState.copy(loading = false, weather = result.weather)
        }
    }

}

