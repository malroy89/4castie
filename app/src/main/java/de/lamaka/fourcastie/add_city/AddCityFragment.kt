package de.lamaka.fourcastie.add_city

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.lamaka.fourcastie.R
import de.lamaka.fourcastie.misc.setNavigationResult

class AddCityFragment : DialogFragment() {

    private var city: EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.fragment_add_city, null)
        city = view.findViewById(R.id.city_name)
        return MaterialAlertDialogBuilder(context)
            .setTitle("Enter city name")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                setResult(city?.text.toString())
            }
            .setNegativeButton("Cancel") { _, _ ->
                setResult(null)
            }
            .show()
    }

    private fun setResult(city: String?) {
        setNavigationResult(city, "ADD_CITY_RESULT")
    }

    // TODO refactor and add validation (blanks)

}