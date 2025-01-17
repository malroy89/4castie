package de.lamaka.fourcastie

import com.android.SdkConstants
import com.android.Version

final class Deps {
    private static final class versions {
        static final String agp = Version.ANDROID_GRADLE_PLUGIN_VERSION
        static final String kotlin = "1.3.72"
        static final String retrofit = "2.9.0"
        static final String material = "1.1.0"
        static final String timber = "4.7.1"
        static final String okhttp = "4.7.2"
        static final String hilt = "2.28-alpha"
        static final String prefs = "1.1.0"
        static final String swipeRefreshLayout = "1.1.0"
        static final String weatherIconView = "1.1.0"

        static final class androidx {
            static final String coreKtx = "1.3.0"
            static final String appcompat = "1.1.0"
            static final String navigation = "2.3.0"
            static final String constraintLayout = "1.1.3"
            static final String lifecycle = "2.2.0"
            static final String hilt = "1.0.0-alpha01"
            static final String activityKtx = "1.2.0-alpha02"
            static final String fragmentKtx = "1.3.0-alpha02"
        }

        static final class test {
            static final String junit = "4.12"
            static final String mockito = "3.4.0"
            static final String truth = "1.0"
            static final String coroutines = "1.3.7"

            static final class androidx {
                static final String junit = "1.1.1"
                static final String espresso = "3.2.0"
                static final String arch = "2.1.0"
            }
        }
    }

    static final class kotlin {
        public static final String stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$versions.kotlin"
        static final String plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$versions.kotlin"
        static final String coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$versions.kotlin"
    }

    static final class retrofit {
        static final String core = "com.squareup.retrofit2:retrofit:$versions.retrofit"
        static final String gsonConverter = "com.squareup.retrofit2:converter-gson:$versions.retrofit"
    }

    static final class okhttp {
        static final String loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$versions.okhttp"
    }

    static final class androidx {
        static final String coreKtx = "androidx.core:core-ktx:$versions.androidx.coreKtx"
        static final String appcompat = "androidx.appcompat:appcompat:$versions.androidx.appcompat"
        static final String constraintLayout = "androidx.constraintlayout:constraintlayout:$versions.androidx.constraintLayout"
        static final String prefs = "androidx.preference:preference:$versions.prefs"
        static final String swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:$versions.swipeRefreshLayout"
        static final String activityKtx = "androidx.activity:activity-ktx:$versions.androidx.activityKtx"
        static final String fragmentKtx = "androidx.fragment:fragment-ktx:$versions.androidx.fragmentKtx"

        static final class lifecycle {
            static final String livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$versions.androidx.lifecycle"
        }

        static final class navigation {
            static final String fragment = "androidx.navigation:navigation-fragment-ktx:$versions.androidx.navigation"
            static final String uiKtx = "androidx.navigation:navigation-ui-ktx:$versions.androidx.navigation"
            static final String safeArgsPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$versions.androidx.navigation"
        }

        static final class hilt {
            static final String lifecycleViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$versions.androidx.hilt"
            static final String compiler = "androidx.hilt:hilt-compiler:$versions.androidx.hilt"
        }
    }

    static final String material = "com.google.android.material:material:$versions.material"
    static final String timber = "com.jakewharton.timber:timber:$versions.timber"
    static final String agp = "${SdkConstants.GRADLE_PLUGIN_NAME}${versions.agp}"
    static final String weatherIconView = "com.github.pwittchen:weathericonview:$versions.weatherIconView"

    static final class hilt {
        static final String plugin = "com.google.dagger:hilt-android-gradle-plugin:$versions.hilt"
        static final String core = "com.google.dagger:hilt-android:$versions.hilt"
        static final String compiler = "com.google.dagger:hilt-android-compiler:$versions.hilt"
    }

    static final class test {
        static final String junit = "junit:junit:$versions.test.junit"
        static final String mockito = "org.mockito:mockito-core:$versions.test.mockito"
        static final String truth = "com.google.truth:truth:${versions.test.truth}"
        static final String coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$versions.test.coroutines"

        static final class androidx {
            static final String junit = "androidx.test.ext:junit:$versions.test.androidx.junit"
            static final String arch = "androidx.arch.core:core-testing:$versions.test.androidx.arch"

            static final class espresso {
                static final String core = "androidx.test.espresso:espresso-core:$versions.test.androidx.espresso"
            }
        }
    }
}