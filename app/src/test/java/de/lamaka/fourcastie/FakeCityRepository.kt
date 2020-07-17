package de.lamaka.fourcastie

import de.lamaka.fourcastie.domain.CityRepository

class FakeCityRepository : CityRepository {

    private val storage = mutableListOf<String>()

    override suspend fun saveCity(name: String) {
        storage.add(name)
    }

    override suspend fun getAllCities(): List<String> = storage
}