plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.gmail_bssushant2003.journeycraft"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gmail_bssushant2003.journeycraft"
        minSdk = 25
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

    // Set JVM target version for Kotlin compilation
    kotlinOptions {
        jvmTarget = "1.8" // Set your desired JVM target version for Kotlin
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
    }
}
dependencies {


    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("com.google.firebase:firebase-messaging:23.2.1")

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.maps:google-maps-services:0.14.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.github.dangiashish:Google-Direction-Api:1.6")

    implementation("com.github.fornewid:neumorphism:0.3.0")

    // Weather
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("com.google.code.gson:gson:2.10")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("com.github.Dimezis:BlurView:version-2.0.3")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.github.MatteoBattilana:WeatherView:3.0.0") {
        exclude(group = "com.github.plattysoft", module = "Leonids")
    }

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("com.google.android.libraries.places:places:4.1.0")
    implementation("androidx.activity:activity:1.9.3")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:23.1.0")

    implementation("com.airbnb.android:lottie:3.4.4")

    implementation("com.opencsv:opencsv:5.6")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")

    // DropDown Menu
    implementation("com.github.qandeelabbassi:Dropsy:1.1")
}
