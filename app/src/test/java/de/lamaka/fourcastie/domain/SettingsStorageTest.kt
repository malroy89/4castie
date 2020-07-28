package de.lamaka.fourcastie.domain

import android.content.Context
import androidx.core.content.edit
import com.google.common.truth.Truth.assertThat
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.data.FakeSharedPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettingsStorageTest {

    @Mock
    private lateinit var context: Context

    private val sharedPreferences = FakeSharedPreferences()

    private lateinit var subject: SettingsStorage

    @Before
    fun setUp() {
        `when`(context.getString(R.string.unit_setting_key)).thenReturn("key")
        `when`(context.getString(R.string.default_unit_value)).thenReturn("defaultValue")
        subject = SettingsStorage(context, sharedPreferences)
    }

    @Test
    fun getSelectedUnit_whenNoValueSaved_shouldReturnStandard() {
        assertThat(subject.getSelectedUnit()).isEqualTo(UnitSystem.STANDARD)
    }

    @Test
    fun getSelectedUnit_whenUnknownValueSaved_shouldReturnStandard() {
        sharedPreferences.edit {
            putString("key", "unknownValue")
        }
        assertThat(subject.getSelectedUnit()).isEqualTo(UnitSystem.STANDARD)
    }

    @Test
    fun getSelectedUnit_whenStandardValueSaved_shouldReturnStandard() {
        sharedPreferences.edit {
            putString("key", "standard")
        }
        assertThat(subject.getSelectedUnit()).isEqualTo(UnitSystem.STANDARD)
    }

    @Test
    fun getSelectedUnit_whenImperialValueSaved_shouldReturnImperial() {
        sharedPreferences.edit {
            putString("key", "imperial")
        }
        assertThat(subject.getSelectedUnit()).isEqualTo(UnitSystem.IMPERIAL)
    }

    @Test
    fun getSelectedUnit_whenMetricValueSaved_shouldReturnMetric() {
        sharedPreferences.edit {
            putString("key", "metric")
        }
        assertThat(subject.getSelectedUnit()).isEqualTo(UnitSystem.METRIC)
    }
}