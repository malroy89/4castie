package de.lamaka.fourcastie.data.interceptor

import de.lamaka.fourcastie.domain.SettingsStorage
import de.lamaka.fourcastie.domain.UnitSystem
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class UnitSettingInterceptor @Inject constructor(
    private val settingsStorage: SettingsStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val newRequest = when (settingsStorage.getSelectedUnit()) {
            UnitSystem.METRIC -> buildNewRequest(originRequest, "metric")
            UnitSystem.IMPERIAL -> buildNewRequest(originRequest, "imperial")
            UnitSystem.STANDARD -> originRequest
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