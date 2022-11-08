plugins {
    id("com.android.library") version Dependencies.Android.agpVersion
    kotlin("android") version Dependencies.Kotlin.version
    kotlin("plugin.parcelize") version Dependencies.Kotlin.version
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    implementation(Dependencies.Napier.core)
    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Decompose.core)
    implementation(Dependencies.Decompose.compose)
    implementation(Dependencies.ZXing.core)
    implementation(Dependencies.Material.design)
    implementation(Dependencies.Camera.core)
    implementation(Dependencies.Camera.camera)
    implementation(Dependencies.Camera.view)
    implementation(Dependencies.Camera.lifecycle)
    implementation(Dependencies.MLKit.scanner)
    implementation(Dependencies.Accompanist.permissions)
    implementation(Dependencies.Material.kalendar)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.material3)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Accompanist.placeholder)
    implementation(Dependencies.Activity.compose)
    debugImplementation(Dependencies.Compose.tooling)
    implementation(Dependencies.Compose.toolingPreview)
}