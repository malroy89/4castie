package de.lamaka.fourcastie.city

data class WeatherView(
    val city: String,
    val description: String,
    val temperature: String,
    val humidity: String,
    val pressure: String,
    val windSpeed: String
)