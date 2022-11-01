plugins {
    id("com.android.library") version Dependencies.Android.agpVersion
    kotlin("plugin.parcelize") version Dependencies.Kotlin.version
    kotlin("android") version Dependencies.Kotlin.version
    id(Dependencies.Ksp.plugin) version Dependencies.Ksp.version
}

android {
    compileSdk = Config.compileSdk

    defaultConfig {
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            LocalConfig.getDebugFields().forEach { (key, value) ->
                buildConfigField(
                    "String",
                    key,
                    "\"$value\"",
                )
            }
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlin {
        explicitApi()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compilerVersion
    }
    androidComponents.onVariants { variant ->
        kotlin.sourceSets.findByName(variant.name)?.kotlin?.srcDirs(
            file("$buildDir/generated/ksp/${variant.name}/kotlin")
        )
    }
}

dependencies {
    implementation(Dependencies.Android.core)
    implementation(Dependencies.Activity.compose)
    implementation(Dependencies.Android.startup)
    implementation(Dependencies.Android.splash)
    implementation(Dependencies.Koin.android)
    implementation(Dependencies.Koin.core)
    implementation(Dependencies.Koin.annotations)
    ksp(Dependencies.Koin.compiler)
    implementation(Dependencies.Coroutines.android)
    implementation(Dependencies.OAuth.android)
    implementation(Dependencies.Decompose.core)
    implementation(Dependencies.MVI.logging)
    implementation(Dependencies.MVI.main)
    implementation(Dependencies.MVI.core)
    implementation(Dependencies.MVI.rx)
    implementation(Dependencies.Ktor.cio)
    implementation(Dependencies.Ktor.contentNegotiation)
    implementation(Dependencies.Ktor.serialization)
    implementation(Dependencies.Ktor.logging)
    implementation(Dependencies.Ktor.auth)
    implementation(Dependencies.Compose.material3)
    implementation(Dependencies.Compose.foundation)
    implementation(projects.appComponents)
    implementation(projects.appComponentsUi)
    implementation(projects.networkMviImpl)
    implementation(projects.networkMviCore)
    implementation(projects.networkApiImpl)
    implementation(projects.networkApiCore)
    implementation(projects.networkLocalImpl)
    implementation(projects.networkLocalCore)
}