plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.ruralconnect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ruralconnect"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase BOM (Bill of Materials) to ensure compatible versions
    implementation(platform(libs.firebase.bom))

    // Firebase Realtime Database
    implementation(libs.firebase.database)

    // Firebase Authentication
    implementation(libs.firebase.auth)

    // Firebase Analytics
    implementation(libs.firebase.analytics)

    // Firebase Storage
    implementation("com.google.firebase:firebase-storage")

    // RecyclerView
    implementation(libs.recyclerview)

    // CardView
    implementation(libs.cardview)
}
