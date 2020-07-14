package de.lamaka.fourcastie.data.model

import com.google.gson.annotations.SerializedName

data class ApiForecastWeather(
    @SerializedName("dt")
    val timestamp: Long,
    @SerializedName("main")
    val apiWeatherCondition: ApiWeatherCondition,
    @SerializedName("weather")
    val apiWeatherDetails: List<ApiWeatherDetails>,
    val wind: ApiWind
)