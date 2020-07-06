package de.lamaka.fourcastie.cities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.misc.isDestinationOnScreen

// TODO add a possibility to remove city from the list
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

        adapter = CitiesAdapter { clickedCity ->
            val action = CitiesFragmentDirections.actionCitiesFragmentToCityFragment(clickedCity)
            findNavController().navigate(action)
        }
        cities?.adapter = adapter

        viewModel.viewState.observe(viewLifecycleOwner) { render(it) }
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
        val navController = findNavController()
        if (!navController.isDestinationOnScreen(R.id.addCity)) {
            navController.navigate(R.id.addCity)
        }

        val navBackStackEntry = navController.getBackStackEntry(R.id.citiesFragment)
        navBackStackEntry.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        val city =
                            navBackStackEntry.savedStateHandle.remove<String>("ADD_CITY_RESULT")
                        if (city.isNullOrEmpty()) {
                            viewModel.handle(CitiesAction.AddCityCancel)
                        } else {
                            viewModel.handle(CitiesAction.FinishAdding(city))
                        }
                    }
                    Lifecycle.Event.ON_DESTROY -> navBackStackEntry.lifecycle.removeObserver(this)
                    else -> {
                        /* nothing */
                    }
                }
            }
        })
    }

}