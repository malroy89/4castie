package de.lamaka.fourcastie.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.content.edit
import com.google.common.truth.Truth.assertThat
import de.lamaka.fourcastie.misc.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SharedPrefsCityRepositoryTest {

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun getAllCities_whenSavedValueIsNull_shouldReturnEmptyList() {
        val subject = SharedPrefsCityRepository(
            coroutinesTestRule.testDispatcherProvider,
            FakeSharedPreferences()
        )

        coroutinesTestRule.runBlockingTest {
            val actual = subject.getAllCities()
            assertThat(actual).isEmpty()
        }
    }

    @Test
    fun getAllCities_whenSavedValueIsEmpty_shouldReturnEmptyList() {
        val sharedPreferences = FakeSharedPreferences()
        sharedPreferences.edit {
            putString("cities", "")
        }

        val subject = SharedPrefsCityRepository(
            coroutinesTestRule.testDispatcherProvider,
            sharedPreferences
        )

        coroutinesTestRule.runBlockingTest {
            val actual = subject.getAllCities()
            assertThat(actual).isEmpty()
        }
    }

    @Test
    fun getAllCities_whenSavedValueIsNotEmpty_shouldReturnListWithValues() {
        val sharedPreferences = FakeSharedPreferences()
        sharedPreferences.edit {
            putString("cities", "city1,city2,city3")
        }

        val subject = SharedPrefsCityRepository(
            coroutinesTestRule.testDispatcherProvider,
            sharedPreferences
        )

        coroutinesTestRule.runBlockingTest {
            val actual = subject.getAllCities()
            assertThat(actual.size).isEqualTo(3)
        }
    }

    @Test
    fun saveCity_whenCurrentValueInStorageIsNull_shouldSavePassedValue() {
        val sharedPreferences = FakeSharedPreferences()
        val subject = SharedPrefsCityRepository(
            coroutinesTestRule.testDispatcherProvider,
            sharedPreferences
        )

        coroutinesTestRule.runBlockingTest {
            subject.saveCity("city1")
            assertThat(sharedPreferences.getString("cities", "")).isEqualTo("city1")
        }
    }

    @Test
    fun saveCity_whenCurrentValueInStorageIsEmpty_shouldSavePassedValue() {
        val sharedPreferences = FakeSharedPreferences()
        sharedPreferences.edit {
            putString("cities", "")
        }

        val subject = SharedPrefsCityRepository(
            coroutinesTestRule.testDispatcherProvider,
            sharedPreferences
        )

        coroutinesTestRule.runBlockingTest {
            subject.saveCity("city1")
            assertThat(sharedPreferences.getString("cities", "")).isEqualTo("city1")
        }
    }

    @Test
    fun saveCity_whenCurrentValueInStorageIsNotEmpty_shouldSavePassedValue() {
        val sharedPreferences = FakeSharedPreferences()
        sharedPreferences.edit {
            putString("cities", "city1")
        }

        val subject = SharedPrefsCityRepository(
            coroutinesTestRule.testDispatcherProvider,
            sharedPreferences
        )

        coroutinesTestRule.runBlockingTest {
            subject.saveCity("city2")
            assertThat(sharedPreferences.getString("cities", "")).isEqualTo("city1,city2")
        }
    }
}