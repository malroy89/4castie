package de.lamaka.fourcastie.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.data.NetworkWeatherRepository
import de.lamaka.fourcastie.data.OpenWeatherApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

const val BASE_URL = "https://api.openweathermap.org"

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Timber.tag("OkHttp").d(message)
        }
    }).apply { level = HttpLoggingInterceptor.Level.BODY  }
    private val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val viewModel: HomeViewModel = HomeViewModel(
        NetworkWeatherRepository(retrofit.create(OpenWeatherApiService::class.java))
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, Observer { render(it) })
        viewModel.handle(Action.LoadWeatherForCity("berlin"))
    }

    private fun render(viewState: ViewState) {
        when (viewState) {
            is ViewState.Loading -> Timber.d("Loading state")
            is ViewState.WeatherLoaded -> Timber.d("Weather loaded ${viewState.weather}")
        }
    }
}