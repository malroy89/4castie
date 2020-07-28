package de.lamaka.fourcastie.domain.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocationDetectorTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var locationManager: LocationManager

    private lateinit var subject: LocationDetector

    @Before
    fun setUp() {
        `when`(context.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManager)
        subject = LocationDetector(context)
    }

    @Test(expected = MissingLocationPermissionException::class)
    fun getGpsProvidedLocation_whenNoPermission_shouldThrowException() {
        `when`(
            context.checkPermission(
                eq(Manifest.permission.ACCESS_FINE_LOCATION),
                anyInt(),
                anyInt()
            )
        ).thenReturn(PackageManager.PERMISSION_DENIED)

        subject.getGpsProvidedLocation()
    }

    @Test
    fun getGpsProvidedLocation_whenGpsProviderDisabled_shouldReturnNull() {
        `when`(
            context.checkPermission(
                eq(Manifest.permission.ACCESS_FINE_LOCATION),
                anyInt(),
                anyInt()
            )
        ).thenReturn(PackageManager.PERMISSION_GRANTED)
        `when`(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false)

        assertThat(subject.getGpsProvidedLocation()).isNull()
    }

    @Test
    fun getGpsProvidedLocation_whenGpsLocationDetected_shouldReturnCorrectResult() {
        `when`(
            context.checkPermission(
                eq(Manifest.permission.ACCESS_FINE_LOCATION),
                anyInt(),
                anyInt()
            )
        ).thenReturn(PackageManager.PERMISSION_GRANTED)
        `when`(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true)

        val location = mock(Location::class.java)
        `when`(location.latitude).thenReturn(12.34)
        `when`(location.longitude).thenReturn(56.78)
        `when`(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(location)

        val actual = subject.getGpsProvidedLocation()
        assertThat(actual).isNotNull()
        assertThat(actual?.latitude).isEqualTo(12.34)
        assertThat(actual?.longitude).isEqualTo(56.78)
    }

    @Test(expected = MissingLocationPermissionException::class)
    fun getNetworkProvidedLocation_whenNoPermissions_shouldThrowException() {
        `when`(
            context.checkPermission(
                eq(Manifest.permission.ACCESS_FINE_LOCATION),
                anyInt(),
                anyInt()
            )
        ).thenReturn(PackageManager.PERMISSION_DENIED)
        `when`(
            context.checkPermission(
                eq(Manifest.permission.ACCESS_COARSE_LOCATION),
                anyInt(),
                anyInt()
            )
        ).thenReturn(PackageManager.PERMISSION_DENIED)

        subject.getNetworkProvidedLocation()
    }

    @Test
    fun getNetworkProvidedLocation_whenNetworkProviderIsDisabled_shouldReturnNull() {
        `when`(
            context.checkPermission(
                eq(Manifest.permission.ACCESS_FINE_LOCATION),
                anyInt(),
                anyInt()
            )
        ).thenReturn(PackageManager.PERMISSION_GRANTED)
        `when`(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false)

        assertThat(subject.getNetworkProvidedLocation()).isNull()
    }

    @Test
    fun getNetworkProvidedLocation_whenNetworkProvidedLocationDetected_shouldReturnCorrectResult() {
        `when`(
            context.checkPermission(
                eq(Manifest.permission.ACCESS_FINE_LOCATION),
                anyInt(),
                anyInt()
            )
        ).thenReturn(PackageManager.PERMISSION_GRANTED)
        `when`(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(true)

        val location = mock(Location::class.java)
        `when`(location.latitude).thenReturn(12.34)
        `when`(location.longitude).thenReturn(56.78)
        `when`(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).thenReturn(location)

        val actual = subject.getNetworkProvidedLocation()

        assertThat(actual).isNotNull()
        assertThat(actual?.latitude).isEqualTo(12.34)
        assertThat(actual?.longitude).isEqualTo(56.78)
    }

}