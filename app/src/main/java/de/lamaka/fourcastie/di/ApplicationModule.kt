package de.lamaka.fourcastie.di

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.preference.PreferenceManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.multibindings.IntoMap
import de.lamaka.fourcastie.BuildConfig
import de.lamaka.fourcastie.InjectFragmentFactory
import de.lamaka.fourcastie.data.*
import de.lamaka.fourcastie.domain.CityRepository
import de.lamaka.fourcastie.domain.SettingsStorage
import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.home.HomeFragment
import de.lamaka.fourcastie.settings.SettingsFragment
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
    fun provideOkHttpClient(unitSettingInterceptor: UnitSettingInterceptor): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(unitSettingInterceptor)
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(unitSettingInterceptor)
                .build()
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

    @SettingsSharedPrefs
    @Provides
    fun provideSettingsSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @CityStorageSharedPrefs
    @Provides
    fun provideCityStorageSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

}

@Module
@InstallIn(ApplicationComponent::class)
abstract class WeatherRepositoryModule {
    @Binds
    abstract fun bindWeatherRepository(repo: NetworkWeatherRepository): WeatherRepository

    @Binds
    abstract fun bindCityRepository(repo: SharedPrefsCityRepository): CityRepository
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class FragmentBindingModule {

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    abstract fun bindSettingsFragment(homeFragment: SettingsFragment): Fragment

    @Binds
    abstract fun bindFragmentFactory(factory: InjectFragmentFactory): FragmentFactory
}