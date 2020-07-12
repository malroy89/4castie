package de.lamaka.fourcastie.data.mapper

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.city.WeatherView
import de.lamaka.fourcastie.domain.SettingsStorage
import de.lamaka.fourcastie.domain.UnitSystem
import de.lamaka.fourcastie.domain.UnitSystem.*
import de.lamaka.fourcastie.domain.model.Weather
import javax.inject.Inject

class WeatherToViewMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsStorage: SettingsStorage
) : Mapper<Weather, WeatherView> {

    private val tempMapping: Map<UnitSystem, (Double) -> String> = mapOf(
        METRIC to ::metricTemperatureLabel,
        IMPERIAL to ::imperialTemperatureLabel,
        STANDARD to ::standardTemperatureLabel
    )

    private val speedMapping: Map<UnitSystem, (Double) -> String> = mapOf(
        METRIC to ::metricAndStandardSpeedLabel,
        IMPERIAL to ::imperialSpeedLabel,
        STANDARD to ::metricAndStandardSpeedLabel
    )

    override fun map(from: Weather): WeatherView {
        val unitSystem = settingsStorage.getSelectedUnit()
        return WeatherView(
            city = from.cityName,
            description = from.description,
            temperature = tempMapping.getValue(unitSystem).invoke(from.temperature),
            feelsLikeTemperature = context.getString(R.string.label_feels_like, tempMapping.getValue(unitSystem).invoke(from.temperatureFeelsLike)),
            humidity = context.getString(R.string.label_humidity, from.humidity.toInt()),
            pressure = context.getString(R.string.label_pressure, from.pressure.toInt()),
            windSpeed = speedMapping.getValue(unitSystem).invoke(from.windSpeed)
        )
    }

    private fun metricAndStandardSpeedLabel(value: Double): String {
        return context.getString(R.string.label_speed_metric_standard, value)
    }

    private fun imperialSpeedLabel(value: Double): String {
        return context.getString(R.string.label_speed_imperial, value)
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

}