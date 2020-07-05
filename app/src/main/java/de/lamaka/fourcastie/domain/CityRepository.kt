package de.lamaka.fourcastie.domain

interface CityRepository {
    suspend fun saveCity(name: String)
    suspend fun getAllCities(): List<String>
}