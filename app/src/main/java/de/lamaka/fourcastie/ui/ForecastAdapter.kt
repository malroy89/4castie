package de.lamaka.fourcastie.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.pwittchen.weathericonview.WeatherIconView
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.misc.showIcon
import de.lamaka.fourcastie.ui.model.ForecastView

class ForecastAdapter(private val forecast: List<ForecastView>) :
    RecyclerView.Adapter<ForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_forecast_item, parent, false)
        return ForecastViewHolder(view)
    }

    override fun getItemCount() = forecast.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = forecast[position]
        holder.date.text = forecast.date
        holder.icon.showIcon(forecast.description)
        holder.temperature.text = forecast.temperature
        holder.description.text = forecast.description
    }
}

class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val date: TextView = view.findViewById(R.id.date)
    val icon: WeatherIconView = view.findViewById(R.id.weather_icon)
    val temperature: TextView = view.findViewById(R.id.temperature)
    val description: TextView = view.findViewById(R.id.description)
}