plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //Safe args
    id("androidx.navigation.safeargs.kotlin")
    //Ksp
    alias(libs.plugins.kotlinAndroidKsp)
    //Hilt
    alias(libs.plugins.hiltAndroid)

    id("kotlin-parcelize")
}

android {
    namespace = "com.example.testappexchange"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.testappexchange"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    //Ksp
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.bundles.navigation)
    //Room
    implementation(libs.bundles.room)
    annotationProcessor(libs.androidx.room.annotation.prcs)
    ksp(libs.androidx.room.annotation.prcs)
    //Retrofit
    implementation(libs.bundles.retrofit)
    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    val lifecycle_version = "2.8.7"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
}