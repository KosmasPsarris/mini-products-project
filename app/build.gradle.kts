plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)

}

android {
    namespace = "com.kosmas.tecprin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kosmas.tecprin"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            matchingFallbacks.add("debug")

            buildConfigField("String", "BASE_API_URL", "\"https://dummyjson.com/\"")
        }
        release {
            matchingFallbacks.add("release")

            buildConfigField("String", "BASE_API_URL", "\"https://dummyjson.com/\"")

            isMinifyEnabled = false
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    testOptions {
        animationsDisabled = true
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.lifecycle.runtime)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.swiperefresh)

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.kapt)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.ok.http)

    implementation(libs.glide)
    ksp(libs.glide.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.uiautomator)
}
