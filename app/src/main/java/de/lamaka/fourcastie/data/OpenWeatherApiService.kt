package de.lamaka.fourcastie.data

import de.lamaka.fourcastie.data.model.ApiWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {

    @GET("/data/2.5/weather")
    suspend fun getWeatherByCityName(
        @Query("q") cityName: String
    ): ApiWeather

    @GET("/data/2.5/weather")
    suspend fun getWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): ApiWeather
}