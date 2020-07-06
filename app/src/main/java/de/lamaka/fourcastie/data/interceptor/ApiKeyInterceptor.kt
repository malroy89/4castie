package de.lamaka.fourcastie.data.interceptor

import de.lamaka.fourcastie.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val newRequest = chain.request()
            .newBuilder()
            .url(
                originRequest.url
                    .newBuilder()
                    .addQueryParameter("appid", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                    .build()
            ).build()
        return chain.proceed(newRequest)
    }
}