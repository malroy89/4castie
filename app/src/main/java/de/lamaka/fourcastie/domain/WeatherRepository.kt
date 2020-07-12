package de.lamaka.fourcastie.domain

import de.lamaka.fourcastie.domain.model.Weather

interface WeatherRepository {

    @Throws(WeatherRepositoryException::class)
    suspend fun loadForCity(name: String): Weather

    @Throws(WeatherRepositoryException::class)
    suspend fun loadForLocation(lat: Double, lng: Double): Weather
}