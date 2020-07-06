package de.lamaka.fourcastie.city

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import de.lamaka.fourcastie.base.BaseViewModel
import de.lamaka.fourcastie.data.mapper.Mapper
import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.domain.WeatherRepositoryException
import de.lamaka.fourcastie.domain.model.Weather
import kotlinx.coroutines.delay

class CityViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository,
    private val mapper: Mapper<Weather, WeatherView>
) : BaseViewModel<CityAction, CityViewState, CityActionResult>(CityAction.Init) {

    override var currentState: CityViewState = CityViewState()

    override fun perform(action: CityAction) = liveData {
        when (action) {
            is CityAction.Load -> {
                emit(CityActionResult.Loading)
                emit(loadWeather(action.cityName))
            }
            CityAction.Init -> {
                emit(CityActionResult.Init)
            }
        }
    }

    private suspend fun loadWeather(cityName: String?): CityActionResult {
        if (cityName.isNullOrEmpty()) {
            return CityActionResult.FailedToLoad("City name is missing. Cannot load data without it")
        }

        return try {
            CityActionResult.Loaded(weatherRepository.loadForCity(cityName))
        } catch (e: WeatherRepositoryException) {
            CityActionResult.FailedToLoad(e.error.message)
        }
    }

    // TODO to think how to make a composite ViewState which would consist of INIT and RESULT(+ params) types
    override fun reduce(result: CityActionResult): CityViewState {
        return when (result) {
            CityActionResult.Init -> CityViewState()
            CityActionResult.Loading -> currentState.copy(
                loading = true,
                weather = null,
                error = null
            )
            is CityActionResult.Loaded -> currentState.copy(
                loading = false,
                weather = mapper.map(result.weather),
                error = null
            )
            is CityActionResult.FailedToLoad -> currentState.copy(
                loading = false,
                weather = null,
                error = result.message
            )
        }
    }

}