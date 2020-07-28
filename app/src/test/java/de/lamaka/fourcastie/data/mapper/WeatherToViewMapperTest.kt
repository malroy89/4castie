package de.lamaka.fourcastie.data.mapper

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import de.lamaka.fourcastie.domain.model.Weather
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherToViewMapperTest {

    @Mock
    private lateinit var labelProvider: LabelProvider

    @Test
    fun map_shouldSetAllValues() {
        `when`(labelProvider.feelsLikeTemperatureLabel(27.1234)).thenReturn("feelsLike")
        `when`(labelProvider.humidityLabel(97.5)).thenReturn("humidity")
        `when`(labelProvider.pressureLabel(123.456)).thenReturn("pressure")
        `when`(labelProvider.temperatureLabel(25.1234)).thenReturn("temperature")
        `when`(labelProvider.speedLabel(15.1234)).thenReturn("windSpeed")

        val from = Weather(
            cityName = "city",
            description = "description",
            windSpeed = 15.1234,
            temperature = 25.1234,
            temperatureFeelsLike = 27.1234,
            pressure = 123.456,
            humidity = 97.5
        )

        val actual = WeatherToViewMapper(labelProvider).map(from)

        assertThat(actual.city).isEqualTo("city")
        assertThat(actual.description).isEqualTo("description")
        assertThat(actual.feelsLikeTemperature).isEqualTo("feelsLike")
        assertThat(actual.humidity).isEqualTo("humidity")
        assertThat(actual.pressure).isEqualTo("pressure")
        assertThat(actual.temperature).isEqualTo("temperature")
        assertThat(actual.windSpeed).isEqualTo("windSpeed")
    }
}