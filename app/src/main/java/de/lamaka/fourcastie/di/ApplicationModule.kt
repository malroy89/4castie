package de.lamaka.fourcastie.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntoMap
import de.lamaka.fourcastie.BuildConfig
import de.lamaka.fourcastie.InjectFragmentFactory
import de.lamaka.fourcastie.data.NetworkWeatherRepository
import de.lamaka.fourcastie.data.OpenWeatherApiService
import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.home.HomeFragment
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            OkHttpClient.Builder().build()
        }
    }

    @Provides
    fun provideBaseUrl() = BuildConfig.API_BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideOpenWeatherApiService(retrofit: Retrofit): OpenWeatherApiService =
        retrofit.create(OpenWeatherApiService::class.java)

}

@Module
@InstallIn(ApplicationComponent::class)
abstract class WeatherRepositoryModule {
    @Binds
    abstract fun bindWeatherRepository(repo: NetworkWeatherRepository): WeatherRepository
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class FragmentBindingModule {

    @Binds
    @IntoMap
    @FragmentKey(HomeFragment::class)
    abstract fun bindHomeFragment(homeFragment: HomeFragment): Fragment

    @Binds
    abstract fun bindFragmentFactory(factory: InjectFragmentFactory): FragmentFactory
}