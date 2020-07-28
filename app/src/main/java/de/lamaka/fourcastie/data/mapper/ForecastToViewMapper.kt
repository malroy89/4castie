package de.lamaka.fourcastie.data.mapper

import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.ui.model.ForecastView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ForecastToViewMapper @Inject constructor(
    private val labelProvider: LabelProvider
) : Mapper<Forecast, ForecastView> {

    override fun map(from: Forecast): ForecastView {
        return ForecastView(
            date = formatTimestamp(from.timestamp * 1000),
            description = from.description,
            temperature = labelProvider.temperatureLabel(from.temperature)
        )
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.ENGLISH)
        return sdf.format(Date(timestamp))
    }
}