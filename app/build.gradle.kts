@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application") version Dependencies.Android.agpVersion
    kotlin("android") version Dependencies.Kotlin.version
}


android {
    compileSdk = Config.compileSdk
    buildToolsVersion = "30.0.3"


    defaultConfig {
        applicationId = Config.packageName
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = 11
        versionName = "0.0.1-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["appAuthRedirectScheme"] = "ru.mclient.oauth"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compilerVersion
    }
}

dependencies {
    implementation(Dependencies.Android.core)
    implementation(Dependencies.Appcompat.core)
    implementation(Dependencies.Koin.android)
    implementation(projects.appStartup)
    implementation(Dependencies.Compose.runtime)
}
