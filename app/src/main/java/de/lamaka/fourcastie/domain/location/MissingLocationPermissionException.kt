package de.lamaka.fourcastie.domain.location

class MissingLocationPermissionException(vararg missingPermission: String) : Exception() {

    val missingPermissions: List<String> = missingPermission.asList()
}