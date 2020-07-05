package de.lamaka.fourcastie.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import de.lamaka.fourcastie.base.BaseViewModel
import de.lamaka.fourcastie.domain.WeatherRepository

class HomeViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository
) : BaseViewModel<HomeAction, HomeViewState, HomeResult>(HomeAction.LoadWeatherForLocation) {

    override fun perform(action: HomeAction) = liveData {
        when (action) {
            is HomeAction.LoadWeatherForCity -> {
                emit(HomeResult.Loading)
                emit(HomeResult.WeatherLoaded(weatherRepository.loadForCity(action.cityName)))
            }
        }
    }

    override var currentState: HomeViewState = HomeViewState()

    override fun reduce(result: HomeResult): HomeViewState {
        return when (result) {
            HomeResult.Loading -> currentState.copy(loading = true)
            is HomeResult.WeatherLoaded -> currentState.copy(loading = false, weather = result.weather)
        }
    }

}

