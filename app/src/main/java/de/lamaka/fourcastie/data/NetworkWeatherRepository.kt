package de.lamaka.fourcastie.data

import de.lamaka.fourcastie.data.ApiRequestException.Type
import de.lamaka.fourcastie.data.model.ApiForecast
import de.lamaka.fourcastie.data.model.ApiWeather
import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.domain.WeatherRepositoryException
import de.lamaka.fourcastie.domain.WeatherRepositoryException.Error
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather
import javax.inject.Inject

private val ERROR_MAPPING = mapOf(
    Type.BAD_RESPONSE to Error.SERVER_ERROR,
    Type.BAD_REQUEST to Error.INVALID_REQUEST,
    Type.NETWORK_ERROR to Error.NETWORK_ERROR
)

private const val FORECASTS_LIMIT = 8

class NetworkWeatherRepository @Inject constructor(
    private val apiService: OpenWeatherApiService
) : WeatherRepository {

    override suspend fun loadWeather(name: String): Weather {
        return loadWeather { apiService.getWeather(name) }
    }

    override suspend fun loadWeather(lat: Double, lng: Double): Weather {
        return loadWeather { apiService.getWeather(lat, lng) }
    }

    override suspend fun loadForecast(name: String): List<Forecast> {
        return loadForecast { apiService.getForecast(name, FORECASTS_LIMIT) }
    }

    override suspend fun loadForecast(lat: Double, lng: Double): List<Forecast> {
        return loadForecast { apiService.getForecast(lat, lng, FORECASTS_LIMIT) }
    }

    private suspend fun loadForecast(apiCall: suspend () -> ApiForecast): List<Forecast> {
        return performSafeCall { apiCall.invoke() }
            .let {
                it.list.map { api ->
                    Forecast(
                        timestamp = api.timestamp,
                        description = api.apiWeatherDetails.first().main,
                        temperature = api.apiWeatherCondition.temp
                    )
                }
            }
    }

    private suspend fun loadWeather(apiCall: suspend () -> ApiWeather): Weather {
        return performSafeCall { apiCall.invoke() }
            .let { weather ->
                Weather(
                    cityName = weather.name,
                    description = weather.apiWeatherDetails.first().main, // refactor
                    windSpeed = weather.wind.speed,
                    temperature = weather.apiWeatherCondition.temp,
                    temperatureFeelsLike = weather.apiWeatherCondition.feelsLike,
                    pressure = weather.apiWeatherCondition.pressure,
                    humidity = weather.apiWeatherCondition.humidity
                )
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