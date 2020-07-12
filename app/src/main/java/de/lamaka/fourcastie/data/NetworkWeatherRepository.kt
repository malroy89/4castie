package de.lamaka.fourcastie.data

import de.lamaka.fourcastie.data.ApiRequestException.Type
import de.lamaka.fourcastie.data.model.ApiWeather
import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.domain.WeatherRepositoryException
import de.lamaka.fourcastie.domain.WeatherRepositoryException.Error
import de.lamaka.fourcastie.domain.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private val ERROR_MAPPING = mapOf(
    Type.BAD_RESPONSE to Error.SERVER_ERROR,
    Type.BAD_REQUEST to Error.INVALID_REQUEST,
    Type.NETWORK_ERROR to Error.NETWORK_ERROR
)

class NetworkWeatherRepository @Inject constructor(
    private val apiService: OpenWeatherApiService
) : WeatherRepository {

    override suspend fun loadForCity(name: String): Weather {
        return loadWeather { apiService.getWeatherByCityName(name) }
    }

    override suspend fun loadForLocation(lat: Double, lng: Double): Weather {
        return loadWeather { apiService.getWeatherByLocation(lat, lng) }
    }

    private suspend fun loadWeather(apiCall: suspend () -> ApiWeather): Weather {
        return withContext(Dispatchers.IO) {
            performSafeCall { apiCall.invoke() }
                .let { weather ->  Weather(
                    weather.name,
                    weather.apiWeatherDetails.first().main, // refactor
                    weather.wind.speed,
                    weather.apiWeatherCondition.temp,
                    weather.apiWeatherCondition.feelsLike,
                    weather.apiWeatherCondition.pressure,
                    weather.apiWeatherCondition.humidity
                ) }
        }
    }

    private suspend fun <T> performSafeCall(apiCall: suspend () -> T): T {
        return try {
            apiCall()
        } catch (e: ApiRequestException) {
            throw WeatherRepositoryException(ERROR_MAPPING[e.type] ?: Error.INTERNAL_ERROR)
        }
    }

}