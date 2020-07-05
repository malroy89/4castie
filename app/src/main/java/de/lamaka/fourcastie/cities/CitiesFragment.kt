package de.lamaka.fourcastie.cities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import de.lamaka.fourcastie.R
import timber.log.Timber

@AndroidEntryPoint
class CitiesFragment : Fragment(R.layout.fragment_cities) {

    private val viewModel: CitiesViewModel by viewModels()

    private var cities: RecyclerView? = null
    private var empty: TextView? = null
    private var adapter: CitiesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empty = view.findViewById(R.id.empty)
        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener { viewModel.handle(CitiesAction.AddCity) }

        cities = view.findViewById(R.id.cities)
        cities?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter = CitiesAdapter { clickedCity -> Timber.d("Clicked city -- $clickedCity") }
        cities?.adapter = adapter

        viewModel.viewState.observe(viewLifecycleOwner, Observer { render(it) })
    }

    private fun render(viewState: CitiesViewState) {
        val newCities = viewState.cities
        if (newCities.isNotEmpty()) {
            cities?.visibility = View.VISIBLE
            adapter?.cities = newCities
            adapter?.notifyDataSetChanged()
            empty?.visibility = View.GONE
        } else {
            cities?.visibility = View.GONE
            empty?.visibility = View.VISIBLE
        }

        if (viewState.addCity) {
            showAddCity()
        }
    }

    private fun showAddCity() {
        val input = EditText(context)
        MaterialAlertDialogBuilder(context)
            .setTitle("Enter city name")
            .setView(input)
            .setPositiveButton("Save") { dialog, which ->
                viewModel.handle(CitiesAction.FinishAdding(input.text.toString()))
            }
            .setNegativeButton("Cancel") { dialog, which ->
                Timber.d("")
            }
            .show()
    }

}