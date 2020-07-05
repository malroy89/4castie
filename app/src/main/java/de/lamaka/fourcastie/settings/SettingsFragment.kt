package de.lamaka.fourcastie.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.domain.SettingsStorage
import javax.inject.Inject

class SettingsFragment @Inject constructor(
    @ApplicationContext context: Context,
    private val settingsStorage: SettingsStorage
) : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val unitPrefId = context.getString(R.string.unit_setting_key)
    private var unitPref: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs, rootKey)

        unitPref = findPreference(unitPrefId)
        unitPref?.summary = settingsStorage.getSelectedUnit()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPref: SharedPreferences?, key: String?) {
        when (key) {
            unitPrefId -> onUnitPrefChanged()
        }
    }

    private fun onUnitPrefChanged() {
        unitPref?.summary = settingsStorage.getSelectedUnit()
    }

}