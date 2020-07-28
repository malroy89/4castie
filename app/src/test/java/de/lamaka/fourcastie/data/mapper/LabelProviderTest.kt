package de.lamaka.fourcastie.data.mapper

import android.content.Context
import com.google.common.truth.Truth.assertThat
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.domain.SettingsStorage
import de.lamaka.fourcastie.domain.UnitSystem
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LabelProviderTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var settingsStorage: SettingsStorage

    private lateinit var subject: LabelProvider

    @Before
    fun setUp() {
        `when`(context.getString(eq(R.string.label_temperature_metric), any())).thenReturn("metricTemperature")
        `when`(context.getString(eq(R.string.label_temperature_imperial), any())).thenReturn("imperialTemperature")
        `when`(context.getString(eq(R.string.label_temperature_standard), any())).thenReturn("standardTemperature")
        `when`(context.getString(eq(R.string.label_speed_imperial), any())).thenReturn("imperialSpeed")
        `when`(context.getString(eq(R.string.label_speed_metric_standard), any())).thenReturn("metricAndStandardSpeed")
        subject = LabelProvider(context, settingsStorage)
    }

    @Test
    fun temperatureLabel_whenMetricSystem_shouldReturnCorrectResult() {
        `when`(settingsStorage.getSelectedUnit()).thenReturn(UnitSystem.METRIC)

        val actual = subject.temperatureLabel(25.1234)

        assertThat(actual).isEqualTo("metricTemperature")
    }

    @Test
    fun temperatureLabel_whenImperialSystem_shouldReturnCorrectResult() {
        `when`(settingsStorage.getSelectedUnit()).thenReturn(UnitSystem.IMPERIAL)

        val actual = subject.temperatureLabel(25.1234)

        assertThat(actual).isEqualTo("imperialTemperature")
    }

    @Test
    fun temperatureLabel_whenStandardSystem_shouldReturnCorrectResult() {
        `when`(settingsStorage.getSelectedUnit()).thenReturn(UnitSystem.STANDARD)

        val actual = subject.temperatureLabel(25.1234)

        assertThat(actual).isEqualTo("standardTemperature")
    }

    @Test
    fun speedLabel_whenMetricSystem_shouldReturnCorrectResult() {
        `when`(settingsStorage.getSelectedUnit()).thenReturn(UnitSystem.METRIC)

        val actual = subject.speedLabel(25.1234)

        assertThat(actual).isEqualTo("metricAndStandardSpeed")
    }

    @Test
    fun speedLabel_whenStandardSystem_shouldReturnCorrectResult() {
        `when`(settingsStorage.getSelectedUnit()).thenReturn(UnitSystem.STANDARD)

        val actual = subject.speedLabel(25.1234)

        assertThat(actual).isEqualTo("metricAndStandardSpeed")
    }

    @Test
    fun speedLabel_whenImperialSystem_shouldReturnCorrectResult() {
        `when`(settingsStorage.getSelectedUnit()).thenReturn(UnitSystem.IMPERIAL)

        val actual = subject.speedLabel(25.1234)

        assertThat(actual).isEqualTo("imperialSpeed")
    }
}