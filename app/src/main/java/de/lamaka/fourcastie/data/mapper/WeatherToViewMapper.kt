package de.lamaka.fourcastie.data.mapper

import de.lamaka.fourcastie.domain.model.Weather
import de.lamaka.fourcastie.ui.model.WeatherView
import javax.inject.Inject

class WeatherToViewMapper @Inject constructor(
    private val labelProvider: LabelProvider
) : Mapper<Weather, WeatherView> {

    override fun map(from: Weather): WeatherView {
        return WeatherView(
            city = from.cityName,
            description = from.description,
            temperature = labelProvider.temperatureLabel(from.temperature),
            feelsLikeTemperature = labelProvider.feelsLikeTemperatureLabel(from.temperatureFeelsLike),
            humidity = labelProvider.humidityLabel(from.humidity),
            pressure = labelProvider.pressureLabel(from.pressure),
            windSpeed = labelProvider.speedLabel(from.windSpeed)
        )
    }

}