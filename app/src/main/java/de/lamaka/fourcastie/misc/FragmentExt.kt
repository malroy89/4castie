package de.lamaka.fourcastie.misc

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun <T> Fragment.setNavigationResult(result: T, key: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}