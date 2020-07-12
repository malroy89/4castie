package de.lamaka.fourcastie.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.pwittchen.weathericonview.WeatherIconView
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.city.WeatherView
import de.lamaka.fourcastie.misc.showIcon

class WeatherDetailsView : ConstraintLayout {

    private var weatherIn: TextView? = null
    private var weatherIcon: WeatherIconView? = null
    private var temperature: TextView? = null
    private var feelsLikeTemperature: TextView? = null
    private var description: TextView? = null
    private var humidity: TextView? = null
    private var pressure: TextView? = null
    private var wind: TextView? = null
    private var weatherDetails: View? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        if (isInEditMode) {
            return
        }

        View.inflate(context, R.layout.layout_weather_details, this)
//        addView(view)
        weatherIn = findViewById(R.id.weather_in_text)
        weatherIcon = findViewById(R.id.weather_icon)
        temperature = findViewById(R.id.weather_temperature)
        feelsLikeTemperature = findViewById(R.id.feels_like_temperature)
        description = findViewById(R.id.weather_main)
        humidity = findViewById(R.id.humidity_text)
        pressure = findViewById(R.id.pressure_text)
        wind = findViewById(R.id.wind_speed_text)
        weatherDetails = findViewById(R.id.weather_det)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    fun render(weather: WeatherView) {
        weatherIn?.text = weather.city
        weatherIcon?.showIcon(weather.description)
        temperature?.text = weather.temperature
        feelsLikeTemperature?.text = weather.feelsLikeTemperature
        description?.text = weather.description
        humidity?.text = weather.humidity
        pressure?.text = weather.pressure
        wind?.text = weather.windSpeed
    }

}