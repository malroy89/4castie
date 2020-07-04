package de.lamaka.fourcastie.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import de.lamaka.fourcastie.R
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment /*@Inject constructor(
    private val viewModel: HomeViewModel
)*/ : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

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