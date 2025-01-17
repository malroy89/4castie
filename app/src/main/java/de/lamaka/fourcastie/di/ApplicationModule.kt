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
import de.lamaka.fourcastie.DefaultDispatcherProvider
import de.lamaka.fourcastie.DispatcherProvider
import de.lamaka.fourcastie.InjectFragmentFactory
import de.lamaka.fourcastie.data.NetworkWeatherRepository
import de.lamaka.fourcastie.data.OpenWeatherApiService
import de.lamaka.fourcastie.data.SharedPrefsCityRepository
import de.lamaka.fourcastie.data.interceptor.ApiKeyInterceptor
import de.lamaka.fourcastie.data.interceptor.ApiRequestInterceptor
import de.lamaka.fourcastie.data.interceptor.UnitSettingInterceptor
import de.lamaka.fourcastie.data.mapper.ForecastToViewMapper
import de.lamaka.fourcastie.data.mapper.Mapper
import de.lamaka.fourcastie.data.mapper.WeatherToViewMapper
import de.lamaka.fourcastie.domain.CityRepository
import de.lamaka.fourcastie.domain.WeatherRepository
import de.lamaka.fourcastie.domain.model.Forecast
import de.lamaka.fourcastie.domain.model.Weather
import de.lamaka.fourcastie.settings.SettingsFragment
import de.lamaka.fourcastie.ui.model.ForecastView
import de.lamaka.fourcastie.ui.model.WeatherView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiRequestInterceptor: ApiRequestInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor,
        unitSettingInterceptor: UnitSettingInterceptor
    ): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
            .addInterceptor(apiRequestInterceptor)
            .addInterceptor(unitSettingInterceptor)
            .addInterceptor(apiKeyInterceptor)
        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(HttpLoggingInterceptor().apply { level = Level.BODY })
        }
        return okHttpBuilder.build()
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

    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider
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
abstract class MapperModule {
    @Binds
    abstract fun bindWeatherToViewMapper(mapper: WeatherToViewMapper): Mapper<Weather, WeatherView>

    @Binds
    abstract fun bindForecastToViewMapper(mapper: ForecastToViewMapper): Mapper<Forecast, ForecastView>
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