package de.lamaka.fourcastie.data.model

import com.google.gson.annotations.SerializedName

data class ApiWeather(
    val id: Int,
    val name: String,
    val wind: ApiWind,
    @SerializedName("weather")
    val apiWeatherDetails: List<ApiWeatherDetails>,
    @SerializedName("main")
    val apiWeatherCondition: ApiWeatherCondition
)


