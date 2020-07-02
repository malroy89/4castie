package de.lamaka.fourcastie.domain.model

data class Weather(
    val cityName: String,
    val description: String,
    val windSpeed: Double,
    val temperature: Double,
    val pressure: Double,
    val humidity: Double
)