plugins {
    id("com.android.library") version Dependencies.Android.agpVersion
    kotlin("plugin.serialization") version Dependencies.Kotlin.version
    kotlin("android") version Dependencies.Kotlin.version
    id(Dependencies.Ksp.plugin) version Dependencies.Ksp.version
    id(Dependencies.Apollo.plugin) version Dependencies.Apollo.version
}

apollo {
    packageName.set("ru.mclient.network.data")
    generateOptionalOperationVariables.set(false)
    generateOperationOutput.set(false)
    mapScalarToKotlinLong("Long")
    mapScalar("LocalDate", "kotlinx.datetime.LocalDate")
    mapScalar("LocalDateTime", "kotlinx.datetime.LocalDateTime")
    mapScalar("LocalTime", "kotlinx.datetime.LocalTime")
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

    buildFeatures {
        buildConfig = false
    }
    androidComponents.onVariants { variant ->
        kotlin.sourceSets.findByName(variant.name)?.kotlin?.srcDirs(
            file("$buildDir/generated/ksp/${variant.name}/kotlin")
        )
    }
}

dependencies {
    implementation(Dependencies.Koin.core)
    implementation(Dependencies.Koin.annotations)
    implementation(Dependencies.Datetime.core)
    ksp(Dependencies.Koin.compiler)
    implementation(Dependencies.Ktor.core)
    implementation(Dependencies.Apollo.runtime)
    implementation(projects.networkApiCore)
}