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
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.material3)
    implementation(Dependencies.Material.design)
    implementation(Dependencies.Android.splash)
    implementation(Dependencies.Activity.compose)
    implementation(Dependencies.Ktor.android)
    implementation(Dependencies.Decompose.core)
    implementation(Dependencies.Decompose.core)
    implementation(projects.appCommon)
    implementation(projects.appComponents)
    implementation(projects.appUi)
}
