package de.lamaka.fourcastie.ui.model

data class WeatherView(
    val city: String,
    val description: String,
    val temperature: String,
    val feelsLikeTemperature: String,
    val humidity: String,
    val pressure: String,
    val windSpeed: String
)