import org.jetbrains.compose.internal.utils.localPropertiesFile
import java.util.Properties

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("com.google.gms.google-services") version "4.4.0"
}

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":client_end_user:shared"))
                //firebase
                implementation(platform("com.google.firebase:firebase-bom:32.4.1"))
            }
        }
    }
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "com.angus.client"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("../../design_system/shared/src/commonMain/resources")

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
    }

    val mapsApiKey = localProperties.getProperty("MAPS_API_KEY") ?: "sss"

    println("mapsApiKey $mapsApiKey ${localProperties.propertyNames().toList().map { it.toString() }}")

    defaultConfig {
        applicationId = "com.angus.enduser"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(libs.versions.jvmToolchain.get().toInt())
    }
}
dependencies {
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.firebase:firebase-common-ktx:20.4.2")
    implementation(libs.firebase.messaging.ktx)
}
