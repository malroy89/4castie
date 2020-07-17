package de.lamaka.fourcastie.data

import de.lamaka.fourcastie.data.model.ApiForecast
import de.lamaka.fourcastie.data.model.ApiWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {

    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("q") cityName: String
    ): ApiWeather

    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): ApiWeather

    @GET("/data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") cityName: String,
        @Query("cnt") count: Int
    ): ApiForecast

    @GET("/data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") count: Int
    ): ApiForecast
}