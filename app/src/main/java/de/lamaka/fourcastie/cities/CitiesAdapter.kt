package de.lamaka.fourcastie.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.lamaka.fourcastie.R

class CitiesAdapter(private val clickListener: (String) -> (Unit)) : RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {

    var cities: List<String> = emptyList()

    class CitiesViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.clickable_simple_list_item_1, parent, false)
        return CitiesViewHolder(view as TextView)
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        val view: TextView = holder.view
        val city = cities[position]
        view.setOnClickListener { clickListener(city) }
        view.text = city
    }
}