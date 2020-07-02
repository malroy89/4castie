package de.lamaka.fourcastie

import com.android.SdkConstants
import com.android.Version
import groovy.transform.CompileStatic

@CompileStatic
final class Deps {
    private static final class versions {
        static final String agp = Version.ANDROID_GRADLE_PLUGIN_VERSION
        static final String kotlin = "1.3.72"
        static final String retrofit = "2.8.1"
        static final String material = "1.1.0"

        static final class androidx {
            static final String coreKtx = "1.3.0"
            static final String appcompat = "1.1.0"
            static final String navigation = "2.3.0"
            static final String constraintLayout = "1.1.3"
        }

        static final class test {
            static final String junit = "4.12"

            static final class androidx {
                static final String junit = "1.1.1"
                static final String espresso = "3.2.0"
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
    }

    static final class androidx {
        static final String coreKtx = "androidx.core:core-ktx:$versions.androidx.coreKtx"
        static final String appcompat = "androidx.appcompat:appcompat:$versions.androidx.appcompat"
        static final String constraintLayout = "androidx.constraintlayout:constraintlayout:$versions.androidx.constraintLayout"

        static final class navigation {
            static final String fragment = "androidx.navigation:navigation-fragment-ktx:$versions.androidx.navigation"
            static final String uiKtx = "androidx.navigation:navigation-ui-ktx:$versions.androidx.navigation"
        }
    }

    static final String material = "com.google.android.material:material:$versions.material"
    public static final String agp = "${SdkConstants.GRADLE_PLUGIN_NAME}${versions.agp}"

    static final class test {
        static final String junit = "junit:junit:$versions.test.junit"

        static final class androidx {
            static final String junit = "androidx.test.ext:junit:$versions.test.androidx.junit"

            static final class espresso {
                static final String core = "androidx.test.espresso:espresso-core:$versions.test.androidx.espresso"
            }
        }
    }
}