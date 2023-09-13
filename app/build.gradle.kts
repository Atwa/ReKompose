@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("kotlinx-serialization")
    kotlin("android")
    kotlin("kapt")
}



val composeVersion = extra.get("compose_version") as String
val reduxKotlin = extra.get("redux_kotlin") as String
val ktorVersion = extra.get("ktor") as String

android {
    namespace = "com.atwa.rekompose"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.atwa.rekompose"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material3:material3:1.0.0-alpha11")

    implementation("org.reduxkotlin:redux-kotlin-compose-jvm:$reduxKotlin")
    implementation("org.reduxkotlin:redux-kotlin-thunk-jvm:$reduxKotlin")
    implementation("org.reduxkotlin:redux-kotlin-threadsafe:$reduxKotlin")

    implementation ("io.ktor:ktor-client-android:$ktorVersion")
    implementation ("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation ("io.ktor:ktor-client-logging:$ktorVersion")

    implementation("com.airbnb.android:lottie-compose:6.1.0")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.31.5-beta")
    implementation("io.coil-kt:coil-compose:2.0.0-rc01")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}