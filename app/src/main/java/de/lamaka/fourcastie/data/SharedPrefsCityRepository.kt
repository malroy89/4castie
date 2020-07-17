package de.lamaka.fourcastie.data

import android.content.SharedPreferences
import androidx.core.content.edit
import de.lamaka.fourcastie.di.CityStorageSharedPrefs
import de.lamaka.fourcastie.domain.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val CITIES_KEY = "cities"
private const val CITY_SEPARATOR = ","

class SharedPrefsCityRepository @Inject constructor(
    @CityStorageSharedPrefs private val sharedPreferences: SharedPreferences
) : CityRepository {

    override suspend fun saveCity(name: String) {
        performAction {
            val currentValue = sharedPreferences.getString(CITIES_KEY, "") ?: ""
            sharedPreferences.edit {
                putString(
                    CITIES_KEY,
                    if (currentValue.isEmpty()) name else "$currentValue$CITY_SEPARATOR$name"
                )
            }
        }
    }

    override suspend fun getAllCities(): List<String> {
        return performAction {
            val value = sharedPreferences.getString(CITIES_KEY, "") ?: ""
            if (value.isEmpty()) {
                emptyList<String>()
            }

            value.split(CITY_SEPARATOR)
        }
    }

    private suspend fun <T> performAction(action: () -> T): T {
        return withContext(Dispatchers.IO) {
            return@withContext action()
        }
    }

}