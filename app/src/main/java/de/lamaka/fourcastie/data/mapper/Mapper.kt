package de.lamaka.fourcastie.data.mapper

interface Mapper<FROM, TO> {
    fun map(from: FROM): TO
}