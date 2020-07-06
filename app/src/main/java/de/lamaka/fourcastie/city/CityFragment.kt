package de.lamaka.fourcastie.city

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.pwittchen.weathericonview.WeatherIconView
import dagger.hilt.android.AndroidEntryPoint
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.misc.showIcon

// TODO make home fragment as a container of a Bottom Nav, in order to make CityFragment fullscreen
// TODO consider usage of ViewBinding
@AndroidEntryPoint
class CityFragment : Fragment(R.layout.fragment_city) {

    private val viewModel: CityViewModel by viewModels()
    private val args: CityFragmentArgs by navArgs()

    private var swipeToRefresh: SwipeRefreshLayout? = null
    private var weatherIn: TextView? = null
    private var date: TextView? = null
    private var weatherIcon: WeatherIconView? = null
    private var temperature: TextView? = null
    private var description: TextView? = null
    private var humidity: TextView? = null
    private var pressure: TextView? = null
    private var wind: TextView? = null
    private var weatherDetails: View? = null

    private var progress: ProgressBar? = null

    private var error: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeToRefresh = view.findViewById(R.id.swipe_refresh_id)
        swipeToRefresh?.setOnRefreshListener {
            viewModel.handle(CityAction.Load(args.cityName))
            swipeToRefresh?.isRefreshing = false
        }

        weatherIn = view.findViewById(R.id.weather_in_text)
        date = view.findViewById(R.id.date_text)
        progress = view.findViewById(R.id.progressBar)
        weatherIcon = view.findViewById(R.id.weather_icon)
        temperature = view.findViewById(R.id.weather_temperature)
        description = view.findViewById(R.id.weather_main)
        error = view.findViewById(R.id.error_text)
        humidity = view.findViewById(R.id.humidity_text)
        pressure = view.findViewById(R.id.pressure_text)
        wind = view.findViewById(R.id.wind_speed_text)
        weatherDetails = view.findViewById(R.id.weather_det)

        viewModel.viewState.observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(state: CityViewState) {
        if (state.isEmpty()) { // a bit hacky :), but for now let's consider that isEmpty means that the fragment has been just initialized
            viewModel.handle(CityAction.Load(args.cityName))
            return
        }

        val progressVisibility = if (state.loading) View.VISIBLE else View.GONE
        progress?.visibility = progressVisibility

        if (state.error != null) {
            error?.text = state.error
            error?.visibility = View.VISIBLE
        } else {
            error?.visibility = View.GONE
        }

        handleWeather(state.weather)
    }

    private fun handleWeather(weather: WeatherView?) {
        if (weather == null) {
            weatherIn?.visibility = View.GONE
            weatherIcon?.visibility = View.GONE
            temperature?.visibility = View.GONE
            description?.visibility = View.GONE
            humidity?.visibility = View.GONE
            pressure?.visibility = View.GONE
            wind?.visibility = View.GONE
            weatherDetails?.visibility = View.GONE
            return
        }

        weatherIn?.text = weather.city
        weatherIn?.visibility = View.VISIBLE

        weatherIcon?.showIcon(weather.description)
        weatherIcon?.visibility = View.VISIBLE

        temperature?.text = weather.temperature
        temperature?.visibility = View.VISIBLE

        description?.text = weather.description
        description?.visibility = View.VISIBLE

        humidity?.text = weather.humidity
        humidity?.visibility = View.VISIBLE

        pressure?.text = weather.pressure
        pressure?.visibility = View.VISIBLE

        wind?.text = weather.windSpeed
        wind?.visibility = View.VISIBLE

        weatherDetails?.visibility = View.VISIBLE
    }
}