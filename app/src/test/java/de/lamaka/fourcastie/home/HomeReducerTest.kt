package de.lamaka.fourcastie.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import de.lamaka.fourcastie.data.mapper.Mapper
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather
import de.lamaka.fourcastie.misc.any
import de.lamaka.fourcastie.ui.model.ForecastView
import de.lamaka.fourcastie.ui.model.WeatherView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(AndroidJUnit4::class)
class HomeReducerTest {

    @Mock
    private lateinit var weatherMapper: Mapper<Weather, WeatherView>

    @Mock
    private lateinit var forecastMapper: Mapper<Forecast, ForecastView>

    private lateinit var subject: HomeReducer

    @Before
    fun setUp() {
        `when`(weatherMapper.map(any())).thenReturn(mock(WeatherView::class.java))
        `when`(forecastMapper.map(any())).thenReturn(mock(ForecastView::class.java))

        subject = HomeReducer(weatherMapper, forecastMapper)
    }

    // default -> loading
    @Test
    fun reduce_whenDefaultState_whenLoadingActionResult_shouldReturnCorrectState() {
        val actual = subject.reduce(HomeViewState(), HomeActionResult.Loading)

        assertLoadingState(actual)
    }

    // loading -> error
    @Test
    fun reduce_whenLoadingState_whenError_shouldReturnCorrectState() {
        val actual = subject.reduce(
            HomeViewState(loading = true),
            HomeActionResult.FailedToLoadWeather("Error")
        )

        assertErrorState(actual)
    }

    // loading -> missing permissions
    @Test
    fun reduce_whenLoadingState_whenNoPermissions_shouldReturnCorrectState() {
        val actual = subject.reduce(
            HomeViewState(loading = true),
            HomeActionResult.MissingPermission(listOf("permission1", "permission2"))
        )

        assertMissingPermissionState(actual, listOf("permission1", "permission2"))
    }

    // loading -> loaded
    @Test
    fun reduce_whenLoadingState_whenLoaded_shouldReturnCorrectState() {
        val actual = subject.reduce(
            HomeViewState(loading = true),
            HomeActionResult.WeatherLoaded(
                mock(Weather::class.java),
                listOf(mock(Forecast::class.java))
            )
        )
        assertLoadedState(actual)
    }

    // loaded -> loading
    @Test
    fun reduce_whenLoadedState_whenLoading_shouldReturnCorrectState() {
        val currentState = HomeViewState(
            weather = mock(WeatherForCity::class.java)
        )
        val actual = subject.reduce(
            currentState,
            HomeActionResult.Loading
        )
        assertLoadingState(actual)
    }

    // error -> loading
    @Test
    fun reduce_whenErrorState_whenLoading_shouldReturnCorrectState() {
        val currentState = HomeViewState(
            error = "error"
        )
        val actual = subject.reduce(
            currentState,
            HomeActionResult.Loading
        )
        assertLoadingState(actual)
    }

    // missing permissions -> loading
    @Test
    fun reduce_whenMissingPermissionsState_whenLoading_shouldReturnCorrectState() {
        val currentState = HomeViewState(
            missingPermissions = listOf("permission1")
        )
        val actual = subject.reduce(
            currentState,
            HomeActionResult.Loading
        )
        assertLoadingState(actual)
    }

    private fun assertLoadingState(state: HomeViewState) {
        assertThat(state.loading).isTrue()
        assertThat(state.error).isNull()
        assertThat(state.missingPermissions).isEmpty()
        assertThat(state.weather).isNull()
    }

    private fun assertMissingPermissionState(
        state: HomeViewState,
        expectedPermissions: List<String>
    ) {
        assertThat(state.loading).isFalse()
        assertThat(state.error).isNull()
        assertThat(state.missingPermissions).isEqualTo(expectedPermissions)
        assertThat(state.weather).isNull()
    }

    private fun assertErrorState(state: HomeViewState) {
        assertThat(state.loading).isFalse()
        assertThat(state.error).isEqualTo("Error")
        assertThat(state.missingPermissions).isEmpty()
        assertThat(state.weather).isNull()
    }

    private fun assertLoadedState(state: HomeViewState) {
        assertThat(state.loading).isFalse()
        assertThat(state.error).isNull()
        assertThat(state.missingPermissions).isEmpty()
        assertThat(state.weather).isNotNull()
    }


}