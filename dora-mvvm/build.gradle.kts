plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    namespace = "dora.mvvm"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    buildFeatures {
        dataBinding = true
        aidl = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
    implementation("androidx.core:core:1.13.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.android.support:design:28.0.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    api("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.6")
    api("androidx.activity:activity-ktx:1.9.2")
    api("androidx.fragment:fragment-ktx:1.8.4")

    api("io.reactivex.rxjava2:rxjava:2.2.19")
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components["release"])
                groupId = "com.github.dora4"
                artifactId = "dora"
                version = "1.2.41"
            }
        }
    }
}
