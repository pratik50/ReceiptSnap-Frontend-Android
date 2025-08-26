plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.pratik.receiptsnap"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pratik.receiptsnap"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
        buildConfig = true
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Lottie Animation
    implementation ("com.airbnb.android:lottie:6.6.7")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.56.1")
    ksp("com.google.dagger:hilt-android-compiler:2.56.1")

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

    // Retrofit + Moshi
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")

    // OkHttp + Chucker
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")
    implementation("androidx.fragment:fragment-ktx:1.8.6")

    //SDP and SSP
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.5")

    //Epoxy
    implementation("com.airbnb.android:epoxy:5.1.4")
    ksp("com.airbnb.android:epoxy-processor:5.1.4")
    implementation ("com.airbnb.android:epoxy-databinding:5.1.4")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(kotlin("test"))

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

    //Loading Button
    implementation(libs.loading.button.android)

    //AnimationBottomBar
    implementation("nl.joery.animatedbottombar:library:1.1.0")

    //Facebook Shimmer effect
    implementation (libs.shimmer)
}