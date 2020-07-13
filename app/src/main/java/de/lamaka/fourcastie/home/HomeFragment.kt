package de.lamaka.fourcastie.home

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.pwittchen.weathericonview.WeatherIconView
import dagger.hilt.android.AndroidEntryPoint
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.city.WeatherView
import de.lamaka.fourcastie.misc.showIcon
import de.lamaka.fourcastie.ui.WeatherDetailsView

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    private var permissions: ActivityResultLauncher<Array<String>>? = null

    private var swipeToRefresh: SwipeRefreshLayout? = null
    private var weatherDetails: WeatherDetailsView? = null
    private var progress: ProgressBar? = null
    private var error: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeToRefresh = view.findViewById(R.id.swipe_refresh_id)
        swipeToRefresh?.setOnRefreshListener {
            viewModel.handle(HomeAction.LoadWeatherForCurrentPosition)
            swipeToRefresh?.isRefreshing = false
        }

        progress = view.findViewById(R.id.progressBar)
        error = view.findViewById(R.id.error_text)
        weatherDetails = view.findViewById(R.id.weather_details)

        viewModel.viewState.observe(viewLifecycleOwner) { render(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        permissions?.dispose()
    }

    private fun render(viewState: HomeViewState) {
        when {
            viewState.loading -> renderLoading()
            viewState.error != null -> renderError(viewState.error)
            viewState.missingPermissions.isNotEmpty() -> renderRequestPermissions(viewState)
            viewState.weather != null -> renderWeather(viewState.weather)
            else -> throw IllegalStateException("Unknown view state $viewState")
        }
    }

    private fun renderRequestPermissions(viewState: HomeViewState) {
        if (viewState.weather == null) {
            weatherDetails?.visibility = View.GONE
        } else {
            renderWeather(viewState.weather)
        }

        progress?.visibility = View.GONE
        error?.visibility = View.GONE

        permissions = prepareCall(ActivityResultContracts.RequestPermissions()) {
            val deniedPermissions = it.entries.filter { entry -> !entry.value }
            viewModel.handle(HomeAction.PermissionsResolved(deniedPermissions.isEmpty()))
        }.also { it.launch(viewState.missingPermissions.toTypedArray()) }
    }

    private fun renderLoading() {
        progress?.visibility = View.VISIBLE
        error?.visibility = View.GONE
        weatherDetails?.visibility = View.GONE
    }

    private fun renderError(message: String) {
        error?.text = message
        error?.visibility = View.VISIBLE

        progress?.visibility = View.GONE
        weatherDetails?.visibility = View.GONE
    }

    private fun renderWeather(weather: WeatherView) {
        progress?.visibility = View.GONE
        error?.visibility = View.GONE

        weatherDetails?.render(weather)
        weatherDetails?.visibility = View.VISIBLE
    }
}