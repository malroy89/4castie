package de.lamaka.fourcastie.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import de.lamaka.fourcastie.DispatcherProvider
import de.lamaka.fourcastie.base.Action
import de.lamaka.fourcastie.base.BaseViewModel
import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.domain.WeatherRepositoryException
import de.lamaka.fourcastie.domain.location.LocationDetector
import de.lamaka.fourcastie.domain.location.MissingLocationPermissionException
import de.lamaka.fourcastie.domain.location.UserLocation
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class HomeViewModel @ViewModelInject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val locationDetector: LocationDetector,
    private val weatherRepository: WeatherRepository,
    homeReducer: HomeReducer
) : BaseViewModel<HomeAction, HomeViewState, HomeActionResult>(
    HomeAction.LoadWeatherForCurrentPosition,
    homeReducer
) {

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
        return withContext(dispatcherProvider.io()) {
            val location: UserLocation?
            try {
                location = locationDetector.getNetworkProvidedLocation()
                    ?: locationDetector.getGpsProvidedLocation()
            } catch (e: MissingLocationPermissionException) {
                return@withContext HomeActionResult.MissingPermission(e.missingPermissions)
            }

            if (location == null) {
                return@withContext HomeActionResult.FailedToLoadWeather("Failed to detect location")
            }

            return@withContext try {
                coroutineScope {
                    val weatherDeferred = async {
                        weatherRepository.loadWeather(
                            location.latitude,
                            location.longitude
                        )
                    }
                    val forecastDeferred = async {
                        weatherRepository.loadForecast(
                            location.latitude,
                            location.longitude
                        )
                    }
                    HomeActionResult.WeatherLoaded(
                        weatherDeferred.await(),
                        forecastDeferred.await()
                    )
                }
            } catch (e: WeatherRepositoryException) {
                HomeActionResult.FailedToLoadWeather(e.error.message)
            }
        }
    }

    override var currentState: HomeViewState = HomeViewState()

}

sealed class HomeAction : Action {
    object LoadWeatherForCurrentPosition : HomeAction()
    data class PermissionsResolved(val allPermissionsGiven: Boolean) : HomeAction()
}

