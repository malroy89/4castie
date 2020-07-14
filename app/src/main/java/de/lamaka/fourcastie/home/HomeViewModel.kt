package de.lamaka.fourcastie.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import de.lamaka.fourcastie.base.BaseViewModel
import de.lamaka.fourcastie.ui.model.WeatherView
import de.lamaka.fourcastie.data.mapper.Mapper
import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.domain.WeatherRepositoryException
import de.lamaka.fourcastie.domain.location.LocationDetector
import de.lamaka.fourcastie.domain.location.MissingLocationPermissionException
import de.lamaka.fourcastie.domain.location.UserLocation
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather
import de.lamaka.fourcastie.ui.model.ForecastView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class HomeViewModel @ViewModelInject constructor(
    private val locationDetector: LocationDetector,
    private val weatherRepository: WeatherRepository,
    private val weatherMapper: Mapper<Weather, WeatherView>,
    private val forecastMapper: Mapper<Forecast, ForecastView>
) : BaseViewModel<HomeAction, HomeViewState, HomeActionResult>(HomeAction.LoadWeatherForCurrentPosition) {

    override fun perform(action: HomeAction) = liveData {
        when (action) {
            is HomeAction.LoadWeatherForCurrentPosition -> {
                emit(HomeActionResult.Loading)
                emit(loadWeatherForCurrentLocation())
            }
            is HomeAction.PermissionsResolved -> {
                if (action.allPermissionsGiven) {
                    emit(HomeActionResult.Loading)
                    emit(loadWeatherForCurrentLocation())
                } else {
                    emit(HomeActionResult.FailedToLoadWeather("No permissions to detect your location"))
                }
            }
        }
    }

    private suspend fun loadWeatherForCurrentLocation(): HomeActionResult {
        return withContext(Dispatchers.IO) {
            val location: UserLocation?
            try {
                location = locationDetector.getNetworkProvidedLocation() ?: locationDetector.getGpsProvidedLocation()
            } catch (e: MissingLocationPermissionException) {
                return@withContext HomeActionResult.MissingPermission(e.missingPermissions)
            }

            if (location == null) {
                return@withContext HomeActionResult.FailedToLoadWeather("Failed to detect location")
            }

            return@withContext try {
                coroutineScope {
                    val weatherDeferred = async { weatherRepository.loadWeather(location.latitude, location.longitude) }
                    val forecastDeferred = async { weatherRepository.loadForecast(location.latitude, location.longitude) }
                    HomeActionResult.WeatherLoaded(weatherDeferred.await(), forecastDeferred.await())
                }
            } catch (e: WeatherRepositoryException) {
                HomeActionResult.FailedToLoadWeather(e.error.message)
            }
        }
    }

    override var currentState: HomeViewState = HomeViewState()

    override fun reduce(result: HomeActionResult): HomeViewState {
        return when (result) {
            HomeActionResult.Loading -> currentState.copy(loading = true)
            is HomeActionResult.WeatherLoaded -> currentState.copy(
                loading = false,
                error = null,
                weather = WeatherForCity(
                    weatherMapper.map(result.weather),
                    result.forecast.map { forecastMapper.map(it) }
                ),
                missingPermissions = emptyList()
            )
            is HomeActionResult.FailedToLoadWeather -> currentState.copy(
                loading = false,
                error = result.message,
                weather = null,
                missingPermissions = emptyList()
            )
            is HomeActionResult.MissingPermission -> currentState.copy(
                loading = false,
                error = null,
                missingPermissions = result.permissions
            )
        }
    }

}

