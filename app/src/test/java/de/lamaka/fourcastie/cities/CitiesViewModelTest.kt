package de.lamaka.fourcastie.cities

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import de.lamaka.fourcastie.misc.CoroutinesTestRule
import de.lamaka.fourcastie.FakeCityRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CitiesViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var subject: CitiesViewModel

    @Before
    fun setUp() = runBlocking {
        subject = CitiesViewModel(
            FakeCityRepository().apply {
                saveCity("Berlin")
                saveCity("Hamburg")
            }
        )

        subject.viewState.observeForever {}

        val actual = subject.viewState.value
        assertThat(actual).isNotNull()
        assertThat(actual?.addCity).isFalse()
        assertThat(actual?.cities).isEqualTo(listOf("Berlin", "Hamburg"))
    }

    @Test
    fun handle_whenAddCityAction_shouldReturnCorrectState() {
        subject.handle(CitiesAction.AddCity)

        val actual = subject.viewState.value
        assertThat(actual).isNotNull()
        assertThat(actual?.addCity).isTrue()
        assertThat(actual?.cities).isEqualTo(listOf("Berlin", "Hamburg"))
    }

    @Test
    fun handle_whenAddCityCancelAction_shouldReturnCorrectState() {
        subject.handle(CitiesAction.AddCityCancel)

        val actual = subject.viewState.value
        assertThat(actual).isNotNull()
        assertThat(actual?.addCity).isFalse()
        assertThat(actual?.cities).isEqualTo(listOf("Berlin", "Hamburg"))
    }

    @Test
    fun handle_whenFinishAddingAction_shouldReturnCorrectState() = runBlocking {
        subject.handle(CitiesAction.FinishAdding("München"))

        val actual = subject.viewState.value
        assertThat(actual).isNotNull()
        assertThat(actual?.addCity).isFalse()
        assertThat(actual?.cities).isEqualTo(listOf("Berlin", "Hamburg", "München"))
    }
}