package de.lamaka.fourcastie

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    fun io(): CoroutineDispatcher = Dispatchers.IO
    fun default(): CoroutineDispatcher = Dispatchers.Default
    fun main(): CoroutineDispatcher = Dispatchers.Main
}