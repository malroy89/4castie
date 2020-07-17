package de.lamaka.fourcastie.city

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import de.lamaka.fourcastie.misc.CoroutinesTestRule
import de.lamaka.fourcastie.FakeWeatherRepository
import de.lamaka.fourcastie.data.mapper.Mapper
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather
import de.lamaka.fourcastie.misc.any
import de.lamaka.fourcastie.ui.model.ForecastView
import de.lamaka.fourcastie.ui.model.WeatherView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CityViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var weatherMapper: Mapper<Weather, WeatherView>
    @Mock
    private lateinit var forecastMapper: Mapper<Forecast, ForecastView>
    @Mock
    private lateinit var observer: Observer<CityViewState>
    @Captor
    private lateinit var viewStateCaptor: ArgumentCaptor<CityViewState>

    private lateinit var subject: CityViewModel

    @Before
    fun setUp() {
        `when`(weatherMapper.map(any())).thenReturn(mock(WeatherView::class.java))
        `when`(forecastMapper.map(any())).thenReturn(mock(ForecastView::class.java))
        subject = CityViewModel(FakeWeatherRepository(), weatherMapper, forecastMapper)
        subject.viewState.observeForever(observer)
    }

    @Test
    fun handle_whenLoadAction_shouldReturnCorrectState() = coroutinesTestRule.runBlockingTest {
        subject.handle(CityAction.Load("Berlin"))

        viewStateCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertThat(allValues[0]).isInstanceOf(CityViewState.Init::class.java)
            assertThat(allValues[1]).isInstanceOf(CityViewState.Loading::class.java)
            assertThat(allValues[2]).isInstanceOf(CityViewState.Loaded::class.java)
        }

        verify(weatherMapper).map(any())
        verify(forecastMapper, times(3)).map(any())
    }

    @Test
    fun handle_whenLoadActionWithEmptyCity_shouldReturnCorrectState() {
        subject.handle(CityAction.Load(""))

        viewStateCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertThat(allValues[0]).isInstanceOf(CityViewState.Init::class.java)
            assertThat(allValues[1]).isInstanceOf(CityViewState.Loading::class.java)
            assertThat(allValues[2]).isInstanceOf(CityViewState.Error::class.java)
        }
    }

    @Test
    fun handle_whenLoadActionWithNullCity_shouldReturnCorrectState() {
        subject.handle(CityAction.Load(null))

        viewStateCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertThat(allValues[0]).isInstanceOf(CityViewState.Init::class.java)
            assertThat(allValues[1]).isInstanceOf(CityViewState.Loading::class.java)
            assertThat(allValues[2]).isInstanceOf(CityViewState.Error::class.java)
        }
    }
}