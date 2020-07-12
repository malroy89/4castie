package de.lamaka.fourcastie.domain.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import de.lamaka.fourcastie.domain.WeatherRepositoryException
import javax.inject.Inject

@SuppressLint("MissingPermission")
class LocationDetector @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Throws(WeatherRepositoryException::class)
    fun getGpsProvidedLocation(): UserLocation? {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            throw MissingLocationPermissionException(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return null
        }

        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            UserLocation(it.latitude, it.longitude)
        }
    }

    @Throws(WeatherRepositoryException::class)
    fun getNetworkProvidedLocation(): UserLocation? {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) &&
            !isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            throw MissingLocationPermissionException(
                Manifest.permission.ACCESS_FINE_LOCATION/*,
                Manifest.permission.ACCESS_COARSE_LOCATION*/
            )
        }

        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return null
        }

        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let {
            UserLocation(it.latitude, it.longitude)
        }
    }

    private fun isPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}