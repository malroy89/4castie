package de.lamaka.fourcastie.domain

import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather

interface WeatherRepository {

    @Throws(WeatherRepositoryException::class)
    suspend fun loadWeather(name: String): Weather

    @Throws(WeatherRepositoryException::class)
    suspend fun loadWeather(lat: Double, lng: Double): Weather

    @Throws(WeatherRepositoryException::class)
    suspend fun loadForecast(name: String): List<Forecast>

    @Throws(WeatherRepositoryException::class)
    suspend fun loadForecast(lat: Double, lng: Double): List<Forecast>
}