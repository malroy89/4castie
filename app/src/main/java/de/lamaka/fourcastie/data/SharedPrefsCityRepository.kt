package de.lamaka.fourcastie.data

import android.content.SharedPreferences
import androidx.core.content.edit
import de.lamaka.fourcastie.di.CityStorageSharedPrefs
import de.lamaka.fourcastie.domain.CityRepository
import kotlinx.coroutines.*
import javax.inject.Inject

private const val CITIES_KEY = "cities"
private const val CITY_SEPARATOR = ","

class SharedPrefsCityRepository @Inject constructor(
    @CityStorageSharedPrefs private val sharedPreferences: SharedPreferences
) : CityRepository {

    // TODO make functions main-safe

    override suspend fun saveCity(name: String) {
        val currentValue = sharedPreferences.getString(CITIES_KEY, "") ?: ""
        sharedPreferences.edit {
            putString(CITIES_KEY, if (currentValue.isEmpty()) name else "$currentValue$CITY_SEPARATOR$name")
        }
    }

    override suspend fun getAllCities(): List<String> {
        val value = sharedPreferences.getString(CITIES_KEY, "") ?: ""
        if (value.isEmpty()) {
            return emptyList()
        }

        return value.split(CITY_SEPARATOR)
    }

}