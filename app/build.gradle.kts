plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.googleDevtoolsKsp)
    alias(libs.plugins.kronosGradlePlugin)

    alias(libs.plugins.lumo)
    // id("com.google.relay") version "0.3.12"
}

android {
    namespace = "smf.talkweb.medal"
    compileSdk = 35

    signingConfigs {
        getByName("debug") {
            storeFile = file("/Users/smfdrummer/Documents/Medal-app/medal.keystore")
            storePassword = "624611"
            keyAlias = "smf"
            keyPassword = "624611"
        }
    }

    defaultConfig {
        applicationId = "smf.talkweb.medal"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        proguardFiles()
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
        aidl = true
    }
    packaging {
        resources {
            excludes += "/META-INF/INDEX.LIST"
        }
    }
}

dependencies {
    implementation(platform(libs.arrow.stack))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.documentfile)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.arrow.suspendApp)
    ksp(libs.arrow.optics.ksp.plugin)

    implementation(libs.bundles.destinations)
    ksp(libs.compose.destinations.ksp)

    implementation(libs.bundles.file)

    implementation(libs.medal.core.android)

    implementation(libs.bundles.koin)
    ksp(libs.koin.ksp.compiler)

    api(libs.composables)
}