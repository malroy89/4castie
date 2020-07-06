package de.lamaka.fourcastie.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import de.lamaka.fourcastie.R
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val cityName = arguments?.getString("cityName")
        val action = if (cityName.isNullOrEmpty()) HomeAction.LoadWeatherForLocation else HomeAction.LoadWeatherForCity(cityName)
        viewModel.handle(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { render(it) }
//        viewModel.handle(Action.LoadWeatherForCity("berlin"))
    }

    private fun render(viewState: HomeViewState) {
        if (viewState.loading) {
            Timber.d("Loading state")
        }

        if (viewState.weather != null) {
            Timber.d("Weather loaded ${viewState.weather}")
        }
    }
}