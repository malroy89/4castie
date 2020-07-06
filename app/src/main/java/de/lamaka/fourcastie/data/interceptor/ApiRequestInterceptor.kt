package de.lamaka.fourcastie.data.interceptor

import de.lamaka.fourcastie.data.ApiRequestException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class ApiRequestInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = try {
            chain.proceed(chain.request())
        } catch (e: IOException) {
            throw ApiRequestException(ApiRequestException.Type.NETWORK_ERROR)
        }

        when (response.code) {
            in 400..420 -> throw ApiRequestException(ApiRequestException.Type.BAD_REQUEST)
            in 500..510 -> throw ApiRequestException(ApiRequestException.Type.BAD_RESPONSE)
        }
        return response
    }

}