package de.lamaka.fourcastie.cities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.add_city.AddCityFragment
import timber.log.Timber
import java.lang.IllegalStateException

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
        /*val input = EditText(context)
        MaterialAlertDialogBuilder(context)
            .setTitle("Enter city name")
            .setView(input)
            .setPositiveButton("Save") { dialog, which ->
                viewModel.handle(CitiesAction.FinishAdding(input.text.toString()))
            }
            .setNegativeButton("Cancel") { dialog, which ->
                Timber.d("")
            }
            .show()*/

        val navController = findNavController()
        if (navController.currentDestination?.id != R.id.addCity) {
            navController.navigate(R.id.addCity)
        }

        val navBackStackEntry = navController.getBackStackEntry(R.id.citiesFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains("ADD_CITY_RESULT")) {
                val result = navBackStackEntry.savedStateHandle.get<AddCityFragment.Result>("ADD_CITY_RESULT")
                navBackStackEntry.savedStateHandle.remove<AddCityFragment.Result>("ADD_CITY_RESULT") // TODO add extension pop?
                val isCancelled = result?.isCancelled ?: true
                if (isCancelled) {
                    viewModel.handle(CitiesAction.AddCityCancel)
                } else {
                    val cityName = result?.cityName ?: throw IllegalStateException("No city name") // TODO может не кидать эксепшен?
                    viewModel.handle(CitiesAction.FinishAdding(cityName))
                }

            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
//        if (navController.currentDestination?.id != R.id.addCity) {
//            navController.navigate(R.id.addCity)
//        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val navController = findNavController();
//        // After a configuration change or process death, the currentBackStackEntry
//        // points to the dialog destination, so you must use getBackStackEntry()
//        // with the specific ID of your destination to ensure we always
//        // get the right NavBackStackEntry
//        val navBackStackEntry = navController.getBackStackEntry(R.id.your_fragment)
//
//        // Create our observer and add it to the NavBackStackEntry's lifecycle
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_RESUME
//                && navBackStackEntry.savedStateHandle.contains("key")) {
//                val result = navBackStackEntry.savedStateHandle.get<String>("key");
//                // Do something with the result
//            }
//        }
//        navBackStackEntry.lifecycle.addObserver(observer)
//
//        // As addObserver() does not automatically remove the observer, we
//        // call removeObserver() manually when the view lifecycle is destroyed
//        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_DESTROY) {
//                navBackStackEntry.lifecycle.removeObserver(observer)
//            }
//        })
//    }

}