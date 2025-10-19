plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.ruralconnect"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.ruralconnect"
        minSdk = 26
        targetSdk = 36
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.google.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation(platform("com.google.firebase:firebase-bom:34.3.0"))
    implementation("com.google.firebase:firebase-analytics")
}
dependencies {
    // Firebase BOM (Bill of Materials) to ensure compatible versions
    implementation platform('com.google.firebase:firebase-bom:32.7.0')

    // Firebase Realtime Database
    implementation 'com.google.firebase:firebase-database'

    // Firebase Authentication (if used)
    implementation 'com.google.firebase:firebase-auth'

    // RecyclerView
    implementation 'androidx.recyclerview:recyclerview:1.3.2'

    // CardView
    implementation 'androidx.cardview:cardview:1.0.0'
}
apply plugin: 'com.google.gms.google-services'
buildscript {
    dependencies {
        classpath 'com.google.gms:google-services:4.4.0'
    }
}
