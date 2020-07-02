package de.lamaka.fourcastie.home

import androidx.lifecycle.*
import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.domain.model.Weather

class HomeViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val nextAction = MutableLiveData<Action>()

    val viewState: LiveData<ViewState> = Transformations.switchMap(nextAction) { action ->
        liveData {
            when (action) {
                is Action.LoadWeatherForCity -> {
                    emit(ViewState.Loading)
                    emit(ViewState.WeatherLoaded(weatherRepository.loadForCity(action.cityName)))
                }
            }
        }
    }

    fun handle(action: Action) {
        nextAction.value = action
    }

}

sealed class Action {
    data class LoadWeatherForCity(val cityName: String) : Action()
}

sealed class ViewState {
    object Loading : ViewState()
    data class WeatherLoaded(val weather: Weather) : ViewState()
}