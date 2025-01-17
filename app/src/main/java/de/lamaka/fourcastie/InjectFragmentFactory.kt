package de.lamaka.fourcastie

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject
import javax.inject.Provider

class InjectFragmentFactory @Inject constructor(
    providers: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {

    private val providers = providers.mapKeys { (fragmentClass, _) -> fragmentClass.name }

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return providers[className]?.get() ?: super.instantiate(classLoader, className)
    }
}