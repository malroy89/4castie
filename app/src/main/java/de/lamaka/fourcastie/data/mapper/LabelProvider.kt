package de.lamaka.fourcastie.data.mapper

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.domain.SettingsStorage
import de.lamaka.fourcastie.domain.UnitSystem
import javax.inject.Inject

class LabelProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsStorage: SettingsStorage
) {

    private val tempMapping: Map<UnitSystem, (Double) -> String> = mapOf(
        UnitSystem.METRIC to ::metricTemperatureLabel,
        UnitSystem.IMPERIAL to ::imperialTemperatureLabel,
        UnitSystem.STANDARD to ::standardTemperatureLabel
    )

    private val speedMapping: Map<UnitSystem, (Double) -> String> = mapOf(
        UnitSystem.METRIC to ::metricAndStandardSpeedLabel,
        UnitSystem.IMPERIAL to ::imperialSpeedLabel,
        UnitSystem.STANDARD to ::metricAndStandardSpeedLabel
    )

    fun temperatureLabel(temperature: Double): String {
        val unitSystem = settingsStorage.getSelectedUnit()
        return tempMapping.getValue(unitSystem).invoke(temperature)
    }

    private fun metricTemperatureLabel(value: Double): String {
        return context.getString(R.string.label_temperature_metric, value.toInt())
    }

    private fun imperialTemperatureLabel(value: Double): String {
        return context.getString(R.string.label_temperature_imperial, value.toInt())
    }

    private fun standardTemperatureLabel(value: Double): String {
        return context.getString(R.string.label_temperature_standard, value.toInt())
    }

    fun speedLabel(speed: Double): String {
        val unitSystem = settingsStorage.getSelectedUnit()
        return speedMapping.getValue(unitSystem).invoke(speed)
    }

    private fun metricAndStandardSpeedLabel(value: Double): String {
        return context.getString(R.string.label_speed_metric_standard, value)
    }

    private fun imperialSpeedLabel(value: Double): String {
        return context.getString(R.string.label_speed_imperial, value)
    }

    fun humidityLabel(humidity: Double): String {
        return context.getString(R.string.label_humidity, humidity.toInt())
    }

    fun pressureLabel(pressure: Double): String {
        return context.getString(R.string.label_pressure, pressure.toInt())
    }

    fun feelsLikeTemperatureLabel(temperatureFeelsLike: Double): String {
        return context.getString(R.string.label_feels_like, temperatureLabel(temperatureFeelsLike))
    }

}