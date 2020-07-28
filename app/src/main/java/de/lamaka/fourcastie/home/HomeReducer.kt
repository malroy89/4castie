package de.lamaka.fourcastie.home

import de.lamaka.fourcastie.base.Reducer
import de.lamaka.fourcastie.data.mapper.Mapper
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather
import de.lamaka.fourcastie.ui.model.ForecastView
import de.lamaka.fourcastie.ui.model.WeatherView
import javax.inject.Inject

class HomeReducer @Inject constructor(
    private val weatherMapper: Mapper<Weather, WeatherView>,
    private val forecastMapper: Mapper<Forecast, ForecastView>
) : Reducer<HomeViewState, HomeActionResult> {

    override fun reduce(currentState: HomeViewState, actionResult: HomeActionResult): HomeViewState {
        return when (actionResult) {
            HomeActionResult.Loading -> currentState.copy(
                loading = true,
                error = null,
                weather = null,
                missingPermissions = emptyList()
            )
            is HomeActionResult.WeatherLoaded -> currentState.copy(
                loading = false,
                error = null,
                weather = WeatherForCity(
                    weatherMapper.map(actionResult.weather),
                    actionResult.forecast.map { forecastMapper.map(it) }
                ),
                missingPermissions = emptyList()
            )
            is HomeActionResult.FailedToLoadWeather -> currentState.copy(
                loading = false,
                error = actionResult.message,
                weather = null,
                missingPermissions = emptyList()
            )
            is HomeActionResult.MissingPermission -> currentState.copy(
                loading = false,
                error = null,
                missingPermissions = actionResult.permissions
            )
        }
    }
}