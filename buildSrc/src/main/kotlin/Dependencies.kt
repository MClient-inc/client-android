object Config {

    const val compileSdk = 33
    const val minSdk = 28
    const val targetSdk = 33
    const val packageName = "ru.mclient.app"

}

interface Dependencies {

    object Datetime : Dependencies {
        const val core = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"
    }

    object OAuth: Dependencies {

        const val android = "net.openid:appauth:0.11.0"

    }

    object Material : Dependencies {
        const val version = "1.8.0-alpha02"
        const val design = "com.google.android.material:material:$version"
        const val kalendar = "com.himanshoe:kalendar:1.2.0"
    }

    object Ksp : Dependencies {
        const val plugin = "com.google.devtools.ksp"
        const val version = "1.7.20-1.0.8"
    }

    object Apollo {
        const val plugin = "com.apollographql.apollo3"
        const val version = "3.4.0"
        const val runtime = "com.apollographql.apollo3:apollo-runtime:$version"
        const val api = "com.apollographql.apollo3:apollo-api:$version"
    }

    object Kotlin : Dependencies {
        const val version = "1.7.20"
    }

    object Napier : Dependencies {
        const val version = "2.6.1"
        const val core = "io.github.aakira:napier:$version"
    }

    object Coroutines : Dependencies {
        const val version = "1.6.4"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val guava = "org.jetbrains.kotlinx:kotlinx-coroutines-guava:$version"
        const val tasks = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$version"
    }

    object Serialization : Dependencies {
        const val version = "1.4.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:$version"
        const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
    }

    object MVI : Dependencies {
        const val version = "3.0.2"
        const val core = "com.arkivanov.mvikotlin:mvikotlin:$version"
        const val main = "com.arkivanov.mvikotlin:mvikotlin-main:$version"
        const val logging = "com.arkivanov.mvikotlin:mvikotlin-logging:$version"
        const val coroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$version"
        const val rx = "com.arkivanov.mvikotlin:rx:$version"
    }

    object Essenty : Dependencies {
        const val version = "0.5.2"
        const val lifecycle = "com.arkivanov.essenty:lifecycle:$version"
        const val parcelable = "com.arkivanov.essenty:parcelable:$version"
        const val instanceKeeper = "com.arkivanov.essenty:instance-keeper:$version"
    }

    object Decompose : Dependencies {
        const val version = "1.0.0-alpha-07"
        const val core = "com.arkivanov.decompose:decompose:$version"
        const val compose = "com.arkivanov.decompose:extensions-compose-jetpack:$version"
    }

    object Destinations : Dependencies {
        const val version = "1.7.26-beta"
        const val core = "io.github.raamcosta.compose-destinations:core:$version"
        const val compiler = "io.github.raamcosta.compose-destinations:ksp:$version"
        const val compose = "com.arkivanov.decompose:extensions-compose-jetpack:$version"
    }

    object Activity : Dependencies {
        const val version = "1.6.1"
        const val core = "androidx.activity:activity-ktx:$version"
        const val compose = "androidx.activity:activity-compose:$version"
    }

    object Appcompat : Dependencies {
        const val version = "1.6.0-rc01"
        const val core = "androidx.appcompat:appcompat:$version"
    }

    object Android : Dependencies {
        const val agpVersion = "7.3.1"
        const val version = "1.9.0"
        const val core = "androidx.core:core-ktx:$version"
        const val splash = "androidx.core:core-splashscreen:1.0.0"
        const val startup = "androidx.startup:startup-runtime:1.1.1"
    }

    object Dialogs: Dependencies {
        const val version = "0.9.0"
        const val core = "io.github.vanpra.compose-material-dialogs:core:${version}"
        const val datetime = "io.github.vanpra.compose-material-dialogs:datetime:${version}"
    }

    object Lifecycle : Dependencies {
        const val version = "2.5.1"
        const val core = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel:$version"
    }

    object Datastore {
        const val version = "1.0.0"
        const val core = "androidx.datastore:datastore-core:$version"
        const val preferences = "androidx.datastore:datastore-preferences:$version"
    }


    object Work {
        const val version = "2.7.1"
        const val core = "androidx.work:work-runtime-ktx:$version"
    }

    object Compose : Dependencies {
        const val compilerVersion = "1.3.2"
        const val version = "1.3.1"
        const val ui = "androidx.compose.ui:ui:$version"
        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val material = "androidx.compose.material:material:$version"
        const val material3 = "androidx.compose.material3:material3:1.0.1"
        const val animation = "androidx.compose.animation:animation:$version"
        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
    }

    object Koin : Dependencies {
        const val version = "3.2.2"
        const val android = "io.insert-koin:koin-android:3.3.0"
        const val core = "io.insert-koin:koin-core:3.2.2"
        const val annotations = "io.insert-koin:koin-annotations:1.0.3"
        const val compiler = "io.insert-koin:koin-ksp-compiler:1.0.3"
        const val compose = "io.insert-koin:koin-androidx-compose:3.3.0"
    }

    object Kodein : Dependencies {
        const val version = "7.14.0"
        const val core = "org.kodein.di:kodein-di:$version"
        const val android = "org.kodein.di:kodein-di-framework-android-x-viewmodel:$version"
        const val conf = "org.kodein.di:kodein-di-conf:$version"
    }

    object Accompanist : Dependencies {
        const val version = "0.27.0"
        const val swiperefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
        const val permissions = "com.google.accompanist:accompanist-permissions:$version"
        const val placeholder = "com.google.accompanist:accompanist-placeholder:$version"
    }

    object Ktor : Dependencies {
        const val version = "2.1.0"
        const val core = "io.ktor:ktor-client-core:$version"
        const val auth = "io.ktor:ktor-client-auth:$version"
        const val android = "io.ktor:ktor-client-android:$version"
        const val cio = "io.ktor:ktor-client-cio:$version"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:$version"
        const val serialization = "io.ktor:ktor-serialization-kotlinx-json:$version"
        const val logging = "io.ktor:ktor-client-logging:$version"
    }


    object Apache : Dependencies {
        const val validators = "commons-validator:commons-validator:1.7"
    }

    object Camera : Dependencies {
        const val version = "1.2.0-rc01"
        const val core = "androidx.camera:camera-core:$version"
        const val camera = "androidx.camera:camera-camera2:$version"
        const val lifecycle = "androidx.camera:camera-lifecycle:$version"
        const val view = "androidx.camera:camera-view:$version"
        const val extensions = "androidx.camera:camera-extensions:$version"
    }

    object Firebase : Dependencies {
        const val BoM = "com.google.firebase:firebase-bom:30.3.1"

        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"

        const val playServicesVersion = "4.3.10"
        const val crashlyticsVersion = "2.8.1"

    }

    object MLKit : Dependencies {
        const val version = "17.0.2"
        const val scanner = "com.google.mlkit:barcode-scanning:$version"
    }

    object ZXing {
        val version = "3.5.0"
        val core = "com.google.zxing:core:$version"
    }

    object Coil : Dependencies {
        const val version = "2.2.2"
        const val core = "io.coil-kt:coil:$version"
        const val compose = "io.coil-kt:coil-compose:$version"
    }

    object Test {
        const val juint = "junit:junit:4.13.2"
        const val android = "androidx.test.ext:junit:1.1.3"
        const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        const val compose = "androidx.compose.ui:ui-test-junit4:${Compose.version}"
    }
}