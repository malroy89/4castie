package de.lamaka.fourcastie.city

import de.lamaka.fourcastie.base.ActionResult
import de.lamaka.fourcastie.base.Reducer
import de.lamaka.fourcastie.base.ViewState
import de.lamaka.fourcastie.data.mapper.Mapper
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather
import de.lamaka.fourcastie.home.WeatherForCity
import de.lamaka.fourcastie.ui.model.ForecastView
import de.lamaka.fourcastie.ui.model.WeatherView
import javax.inject.Inject

class CityReducer @Inject constructor(
    private val mapper: Mapper<Weather, WeatherView>,
    private val forecastMapper: Mapper<Forecast, ForecastView>
) : Reducer<CityViewState, CityActionResult> {
    override fun reduce(
        currentState: CityViewState,
        actionResult: CityActionResult
    ): CityViewState {
        return when (actionResult) {
            CityActionResult.Init -> CityViewState.Init
            CityActionResult.Loading -> CityViewState.Loading
            is CityActionResult.Loaded -> CityViewState.Loaded(
                WeatherForCity(
                    mapper.map(actionResult.weather),
                    actionResult.forecast.map { forecastMapper.map(it) }
                )
            )
            is CityActionResult.FailedToLoad -> CityViewState.Error(actionResult.message)
        }
    }
}

sealed class CityActionResult : ActionResult {
    object Init : CityActionResult()
    object Loading : CityActionResult()
    data class Loaded(val weather: Weather, val forecast: List<Forecast>) : CityActionResult()
    data class FailedToLoad(val message: String) : CityActionResult()
}

sealed class CityViewState : ViewState {
    object Init : CityViewState()
    object Loading : CityViewState()
    data class Error(val message: String) : CityViewState()
    data class Loaded(val weather: WeatherForCity) : CityViewState()
}