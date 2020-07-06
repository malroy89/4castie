package de.lamaka.fourcastie.misc

import androidx.annotation.IdRes
import androidx.navigation.NavController

fun NavController.isDestinationOnScreen(@IdRes resId: Int) = currentDestination?.id == resId


