package de.lamaka.fourcastie.data

import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.domain.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkWeatherRepository @Inject constructor(
    private val apiService: OpenWeatherApiService
) : WeatherRepository {

    // Coroutines Flow?
    override suspend fun loadForCity(name: String): Weather {
        return withContext(Dispatchers.IO) {
            val response = apiService.getWeatherByCityName(name) // TODO catch any exception
            if (!response.isSuccessful) throw RuntimeException()
            val apiWeather = response.body() ?: throw RuntimeException()
            return@withContext Weather(
                apiWeather.name,
                apiWeather.apiWeatherDetails.first().main, // refactor
                apiWeather.wind.speed,
                apiWeather.apiWeatherCondition.temp,
                apiWeather.apiWeatherCondition.pressure,
                apiWeather.apiWeatherCondition.humidity
            )
        }
    }




}