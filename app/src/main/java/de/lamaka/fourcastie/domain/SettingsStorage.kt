package de.lamaka.fourcastie.domain

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.di.SettingsSharedPrefs
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsStorage @Inject constructor(
    @ApplicationContext context: Context,
    @SettingsSharedPrefs private val sharedPrefs: SharedPreferences
) {

    private val unitSettingKey: String = context.getString(R.string.unit_setting_key)
    private val unitSettingDefValue: String = context.getString(R.string.default_unit_value)

    fun getSelectedUnit(): UnitSystem {
        val unitString =
            sharedPrefs.getString(unitSettingKey, unitSettingDefValue) ?: unitSettingDefValue
        return try {
            UnitSystem.valueOf(unitString.toUpperCase(Locale.ENGLISH))
        } catch (e: IllegalArgumentException) {
            Timber.d("An unexpected unit-value \"$unitString\" came from settings. Falling back to \"standard\" unit")
            UnitSystem.STANDARD
        }
    }

}