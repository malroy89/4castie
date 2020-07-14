package de.lamaka.fourcastie.domain.model

data class Forecast(
    val timestamp: Long,
    val description: String,
    val temperature: Double
)