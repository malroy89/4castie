package de.lamaka.fourcastie.data

import de.lamaka.fourcastie.domain.SettingsStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class UnitSettingInterceptor @Inject constructor(
    private val settingsStorage: SettingsStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val selectedUnit = settingsStorage.getSelectedUnit()
        val newRequest = when (selectedUnit.toLowerCase(Locale.ENGLISH)) {
            "metric" -> buildNewRequest(originRequest, "metric")
            "imperial" -> buildNewRequest(originRequest, "imperial")
            "standard" -> originRequest
            else -> {
                Timber.d("An unexpected unit-value \"$selectedUnit\" came from settings. Falling back to \"standard\" unit")
                originRequest
            }
        }
        return chain.proceed(newRequest)
    }

    private fun buildNewRequest(originRequest: Request, unit: String): Request {
        val httpUrl = originRequest.url
            .newBuilder()
            .addQueryParameter("units", unit)
            .build()

        return originRequest.newBuilder().url(httpUrl).build()
    }

}