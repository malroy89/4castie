package de.lamaka.fourcastie.city

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import de.lamaka.fourcastie.base.BaseViewModel
import de.lamaka.fourcastie.data.mapper.Mapper
import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.domain.WeatherRepositoryException
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather
import de.lamaka.fourcastie.home.WeatherForCity
import de.lamaka.fourcastie.ui.model.ForecastView
import de.lamaka.fourcastie.ui.model.WeatherView
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CityViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository,
    private val mapper: Mapper<Weather, WeatherView>,
    private val forecastMapper: Mapper<Forecast, ForecastView>
) : BaseViewModel<CityAction, CityViewState, CityActionResult>(CityAction.Init) {

    override var currentState: CityViewState = CityViewState.Init

    override fun perform(action: CityAction) = liveData {
        when (action) {
            CityAction.Init -> {
                emit(CityActionResult.Init)
            }
            is CityAction.Load -> {
                emit(CityActionResult.Loading)
                emit(loadWeather(action.cityName))
            }
        }
    }

    private suspend fun loadWeather(cityName: String?): CityActionResult {
        if (cityName.isNullOrEmpty()) {
            return CityActionResult.FailedToLoad("City name is missing. Cannot load data without it")
        }

        return try {
            coroutineScope {
                val weatherDeferred = async { weatherRepository.loadWeather(cityName) }
                val forecastDeferred = async { weatherRepository.loadForecast(cityName) }
                CityActionResult.Loaded(weatherDeferred.await(), forecastDeferred.await())
            }
        } catch (e: WeatherRepositoryException) {
            CityActionResult.FailedToLoad(e.error.message)
        }
    }

    override fun reduce(result: CityActionResult): CityViewState {
        return when (result) {
            CityActionResult.Init -> CityViewState.Init
            CityActionResult.Loading -> CityViewState.Loading
            is CityActionResult.Loaded -> CityViewState.Loaded(
                WeatherForCity(
                    mapper.map(result.weather),
                    result.forecast.map { forecastMapper.map(it) }
                )
            )
            is CityActionResult.FailedToLoad -> CityViewState.Error(result.message)
        }
    }



}