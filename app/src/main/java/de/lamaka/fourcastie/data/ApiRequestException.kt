package de.lamaka.fourcastie.data

import java.io.IOException

class ApiRequestException : IOException {

    val type: Type

    constructor(type: Type, message: String) : super(message) {
        this.type = type
    }

    constructor(type: Type) : this(type, type.description)

    enum class Type(val description: String) {
        BAD_REQUEST("Client sent invalid request."),
        BAD_RESPONSE("Internal server error."),
        NETWORK_ERROR("Connectivity issue or timeout.")
    }
}