package de.lamaka.fourcastie.home

import android.Manifest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import de.lamaka.fourcastie.FakeWeatherRepository
import de.lamaka.fourcastie.domain.WeatherRepositoryException
import de.lamaka.fourcastie.domain.location.LocationDetector
import de.lamaka.fourcastie.domain.location.MissingLocationPermissionException
import de.lamaka.fourcastie.domain.location.UserLocation
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather
import de.lamaka.fourcastie.misc.CoroutinesTestRule
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
class HomeViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var locationDetector: LocationDetector

    @Mock
    private lateinit var reducer: HomeReducer

    @Mock
    private lateinit var observer: Observer<HomeViewState>

    @Captor
    private lateinit var viewStateCaptor: ArgumentCaptor<HomeViewState>

    private val weatherRepository = spy(FakeWeatherRepository())

    private lateinit var subject: HomeViewModel

    @Before
    fun setUp() {
        subject = HomeViewModel(
            coroutinesTestRule.testDispatcherProvider,
            locationDetector,
            weatherRepository,
            reducer
        )
    }

    @Test
    fun handle_whenLoadWeatherForLocation_whenNoPermissions_shouldReturnCorrectState() {
        `when`(locationDetector.getNetworkProvidedLocation()).thenThrow(
            MissingLocationPermissionException(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        subject.viewState.observeForever(observer)

        viewStateCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertLoadingState(allValues[0])
            assertMissingPermissionState(allValues[1])
        }
    }

    @Test
    fun handle_whenLoadWeatherForLocation_whenNoPermissions_whenRequestedPermissionsNotGiven_shouldReturnCorrectState() {
        `when`(locationDetector.getNetworkProvidedLocation()).thenThrow(
            MissingLocationPermissionException(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        subject.viewState.observeForever(observer)
        subject.handle(HomeAction.PermissionsResolved(false))

        viewStateCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertLoadingState(allValues[0])
            assertMissingPermissionState(allValues[1])
            assertErrorState(allValues[2])
        }
    }

    @Test
    fun handle_whenLoadWeatherForLocation_whenNoPermissions_whenRequestedPermissionsGiven_shouldReturnCorrectState() =
        coroutinesTestRule.runBlockingTest {
            `when`(locationDetector.getNetworkProvidedLocation())
                .thenThrow(
                    MissingLocationPermissionException(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                .thenReturn(UserLocation(12.0, 34.0))
            subject.viewState.observeForever(observer)

            // MissingPermissionsState -> LoadedState
            `when`(locationDetector.getNetworkProvidedLocation()).thenReturn(
                UserLocation(12.0, 34.0)
            )
            `when`(weatherRepository.loadWeather(12.0, 34.0)).thenReturn(
                mock(Weather::class.java)
            )
            `when`(weatherRepository.loadForecast(12.0, 34.0)).thenReturn(
                listOf(mock(Forecast::class.java))
            )
            subject.handle(HomeAction.PermissionsResolved(true))

            viewStateCaptor.run {
                verify(observer, times(4)).onChanged(capture())
                assertLoadingState(allValues[0])
                assertMissingPermissionState(allValues[1])
                assertLoadingState(allValues[2])
                assertLoadedState(allValues[3])
            }
        }

    @Test
    fun handle_whenLoadWeatherForLocation_whenLocationIsNull_shouldReturnCorrectState() {
        subject.viewState.observeForever(observer)

        viewStateCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertLoadingState(allValues[0])
            assertErrorState(allValues[1])
        }
    }

    @Test
    fun handle_whenLoadWeatherForLocation_whenErrorLoadingWeather_shouldReturnCorrectState() =
        coroutinesTestRule.runBlockingTest {
            `when`(locationDetector.getNetworkProvidedLocation()).thenReturn(
                UserLocation(12.0, 34.0)
            )
            `when`(weatherRepository.loadWeather(12.0, 34.0)).thenThrow(
                WeatherRepositoryException(WeatherRepositoryException.Error.SERVER_ERROR)
            )

            subject.viewState.observeForever(observer)

            viewStateCaptor.run {
                verify(observer, times(2)).onChanged(capture())
                assertLoadingState(allValues[0])
                assertErrorState(allValues[1])
            }
        }

    @Test
    fun handle_whenLoadWeatherForLocation_whenErrorLoadingForecast_shouldReturnCorrectState() =
        coroutinesTestRule.runBlockingTest {
            `when`(locationDetector.getNetworkProvidedLocation()).thenReturn(
                UserLocation(12.0, 34.0)
            )
            `when`(weatherRepository.loadWeather(12.0, 34.0)).thenReturn(mock(Weather::class.java))
            `when`(weatherRepository.loadForecast(12.0, 34.0)).thenThrow(
                WeatherRepositoryException(WeatherRepositoryException.Error.SERVER_ERROR)
            )

            subject.viewState.observeForever(observer)

            viewStateCaptor.run {
                verify(observer, times(2)).onChanged(capture())
                assertLoadingState(allValues[0])
                assertErrorState(allValues[1])
            }
        }

    @Test
    fun handle_whenLoadWeatherForLocation_whenNetworkProvidedLocationIsNotNull_shouldNotCallGpsProvided() =
        coroutinesTestRule.runBlockingTest {
            `when`(locationDetector.getNetworkProvidedLocation()).thenReturn(
                UserLocation(12.0, 34.0)
            )
            `when`(locationDetector.getGpsProvidedLocation()).thenReturn(
                UserLocation(56.0, 78.0)
            )
            `when`(weatherRepository.loadWeather(12.0, 34.0)).thenReturn(
                mock(Weather::class.java)
            )
            `when`(weatherRepository.loadForecast(12.0, 34.0)).thenReturn(
                listOf(mock(Forecast::class.java))
            )

            subject.viewState.observeForever(observer)

            viewStateCaptor.run {
                verify(observer, times(2)).onChanged(capture())
                assertLoadingState(allValues[0])
                assertLoadedState(allValues[1])
            }
        }

    @Test
    fun handle_whenLoadWeatherForLocation_whenNetworkProvidedLocationIsNull_shouldCallGpsProvided() =
        coroutinesTestRule.runBlockingTest {
            `when`(locationDetector.getNetworkProvidedLocation()).thenReturn(null)
            `when`(locationDetector.getGpsProvidedLocation()).thenReturn(
                UserLocation(56.0, 78.0)
            )
            `when`(weatherRepository.loadWeather(56.0, 78.0)).thenReturn(mock(Weather::class.java))
            `when`(weatherRepository.loadForecast(56.0, 78.0)).thenReturn(
                listOf(mock(Forecast::class.java))
            )

            subject.viewState.observeForever(observer)

            viewStateCaptor.run {
                verify(observer, times(2)).onChanged(capture())
                assertLoadingState(allValues[0])
                assertLoadedState(allValues[1])
            }
        }

    // TODO check states when for example from loaded to error and so on

    private fun assertLoadingState(state: HomeViewState) {
        assertThat(state.loading).isTrue()
        assertThat(state.error).isNull()
        assertThat(state.missingPermissions).isEmpty()
        assertThat(state.weather).isNull()
    }

    private fun assertMissingPermissionState(state: HomeViewState) {
        assertThat(state.loading).isFalse()
        assertThat(state.error).isNull()
        assertThat(state.missingPermissions).isNotEmpty()
        assertThat(state.weather).isNull()
    }

    private fun assertErrorState(state: HomeViewState) {
        assertThat(state.loading).isFalse()
        assertThat(state.error).isNotEmpty()
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