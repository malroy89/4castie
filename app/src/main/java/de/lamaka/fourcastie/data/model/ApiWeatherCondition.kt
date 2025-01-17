package de.lamaka.fourcastie.data.model

import com.google.gson.annotations.SerializedName

data class ApiWeatherCondition(
    val temp: Double,
    val pressure: Double,
    val humidity: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double
)