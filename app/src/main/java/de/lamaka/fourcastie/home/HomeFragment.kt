package de.lamaka.fourcastie.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import de.lamaka.fourcastie.R
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, Observer { render(it) })
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