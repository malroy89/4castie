package de.lamaka.fourcastie.domain

class WeatherRepositoryException(val error: Error) : RuntimeException() {

    enum class Error(val message: String) {
        SERVER_ERROR("Server responded with invalid response"),
        INVALID_REQUEST("Failed to load weather due to invalid request. Please, check request parameters"),
        NETWORK_ERROR("Failed to load weather due to connectivity issue"),
        INTERNAL_ERROR("Failed to load weather due to the app's internal issue")
    }
}