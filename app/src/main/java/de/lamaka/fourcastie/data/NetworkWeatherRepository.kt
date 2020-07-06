package de.lamaka.fourcastie.data

import de.lamaka.fourcastie.data.ApiRequestException.Type
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
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getWeatherByCityName(name)
                return@withContext Weather(
                    response.name,
                    response.apiWeatherDetails.first().main, // refactor
                    response.wind.speed,
                    response.apiWeatherCondition.temp,
                    response.apiWeatherCondition.pressure,
                    response.apiWeatherCondition.humidity
                )
            } catch (e: ApiRequestException) {
                throw WeatherRepositoryException(ERROR_MAPPING[e.type] ?: Error.INTERNAL_ERROR)
            }
        }
    }

}