package de.lamaka.fourcastie

import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather
import org.mockito.Mockito.mock

class FakeWeatherRepository : WeatherRepository {
    override suspend fun loadWeather(name: String): Weather {
        return mock(Weather::class.java)
    }

    override suspend fun loadWeather(lat: Double, lng: Double): Weather {
        return mock(Weather::class.java)
    }

    override suspend fun loadForecast(name: String): List<Forecast> {
        return listOf(mock(Forecast::class.java), mock(Forecast::class.java), mock(Forecast::class.java))
    }

    override suspend fun loadForecast(lat: Double, lng: Double): List<Forecast> {
        return listOf(mock(Forecast::class.java), mock(Forecast::class.java), mock(Forecast::class.java))
    }
}