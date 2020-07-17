package de.lamaka.fourcastie.domain.location

class MissingLocationPermissionException(vararg missingPermission: String) : RuntimeException() {

    val missingPermissions: List<String> = missingPermission.asList()
}