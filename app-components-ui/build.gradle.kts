plugins {
    id("com.android.library") version Dependencies.Android.agpVersion
    kotlin("plugin.parcelize") version Dependencies.Kotlin.version
    kotlin("android") version Dependencies.Kotlin.version
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
        buildConfig = false
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compilerVersion
    }

}

dependencies {
    implementation(Dependencies.Android.core)
    implementation(Dependencies.Activity.compose)
    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Decompose.core)
    implementation(Dependencies.Material.design)
    implementation(Dependencies.Decompose.compose)
    implementation(Dependencies.Datetime.core)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.material3)
    implementation(Dependencies.Compose.material)
    implementation(projects.appCommon)
    implementation(projects.appUi)
}