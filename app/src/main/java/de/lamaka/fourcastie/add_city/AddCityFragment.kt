package de.lamaka.fourcastie.add_city

import android.app.Dialog
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.lamaka.fourcastie.R

class AddCityFragment : DialogFragment() {

    private var city: EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.fragment_add_city, null)
        city = view.findViewById(R.id.city_name)
        return MaterialAlertDialogBuilder(context)
            .setTitle("Enter city name")
            .setView(view)
            .setPositiveButton("Save") { dialog, which ->
                findNavController().previousBackStackEntry?.savedStateHandle?.set("ADD_CITY_RESULT", Result(cityName = city?.text.toString()))
            }
            .setNegativeButton("Cancel") { dialog, which ->
                findNavController().previousBackStackEntry?.savedStateHandle?.set("ADD_CITY_RESULT", Result(isCancelled = true))
            }
            .create()
    }

    // TODO refactor and add validation (blanks)

    data class Result(val isCancelled: Boolean = false, val cityName: String? = null) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeByte(if (isCancelled) 1 else 0)
            parcel.writeString(cityName)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Result> {
            override fun createFromParcel(parcel: Parcel): Result {
                return Result(parcel)
            }

            override fun newArray(size: Int): Array<Result?> {
                return arrayOfNulls(size)
            }
        }
    }

}