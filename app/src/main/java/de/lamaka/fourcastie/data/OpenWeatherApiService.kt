package de.lamaka.fourcastie.data

import de.lamaka.fourcastie.BuildConfig
import de.lamaka.fourcastie.data.model.ApiWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY

interface OpenWeatherApiService {

    @GET("/data/2.5/weather?appid=$API_KEY")
    suspend fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("units") units: String
    ): Response<ApiWeather>

    @GET("/data/2.5/weather?appid=$API_KEY")
    suspend fun getWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<ApiWeather>
}