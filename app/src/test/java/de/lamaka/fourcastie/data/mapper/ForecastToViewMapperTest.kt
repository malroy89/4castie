package de.lamaka.fourcastie.data.mapper

import com.google.common.truth.Truth.assertThat
import de.lamaka.fourcastie.domain.model.Forecast
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ForecastToViewMapperTest {

    @Mock
    private lateinit var labelProvider: LabelProvider

    @Test
    fun map_shouldSetAllValues() {
        `when`(labelProvider.temperatureLabel(25.123)).thenReturn("25 C")

        val from = Forecast(1595948433, "description", 25.123) // 07/28/2020 @ 3:00pm (UTC)
        val actual = ForecastToViewMapper(labelProvider).map(from)

        assertThat(actual.date).isEqualTo("28 Jul, 17:00")
        assertThat(actual.description).isEqualTo("description")
        assertThat(actual.temperature).isEqualTo("25 C")
    }
}