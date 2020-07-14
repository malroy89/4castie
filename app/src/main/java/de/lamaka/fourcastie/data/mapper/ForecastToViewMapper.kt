package de.lamaka.fourcastie.data.mapper

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.domain.SettingsStorage
import de.lamaka.fourcastie.domain.UnitSystem
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.ui.model.ForecastView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ForecastToViewMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsStorage: SettingsStorage
) : Mapper<Forecast, ForecastView> {

    private val tempMapping: Map<UnitSystem, (Double) -> String> = mapOf(
        UnitSystem.METRIC to ::metricTemperatureLabel,
        UnitSystem.IMPERIAL to ::imperialTemperatureLabel,
        UnitSystem.STANDARD to ::standardTemperatureLabel
    )

    override fun map(from: Forecast): ForecastView {
        val unitSystem = settingsStorage.getSelectedUnit()
        return ForecastView(
            date = formatTimestamp(from.timestamp * 1000),
            description = from.description,
            temperature = tempMapping.getValue(unitSystem).invoke(from.temperature)
        )
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

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.ENGLISH)
        return sdf.format(Date(timestamp))
    }
}