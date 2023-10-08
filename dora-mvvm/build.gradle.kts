plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    val compile_sdk = 33
    val min_sdk = 21
    val target_sdk = 33
    namespace = "dora.mvvm"
    compileSdk = compile_sdk
    defaultConfig {
        minSdk = min_sdk
        targetSdk = target_sdk
    }
    buildFeatures {
        dataBinding = true
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("com.android.support:design:28.0.0")
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components["release"])
                groupId = "com.github.dora4"
                artifactId = "dora"
                version = "1.1.35"
            }
        }
    }
}
