package de.lamaka.fourcastie.data.model

data class ApiWeatherDetails(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)