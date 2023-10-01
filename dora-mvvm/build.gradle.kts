plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    val compile_sdk = 30
    val min_sdk = 21
    val target_sdk = 30
    val build_tools_version = "30.0.0"
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
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.8.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
    implementation("com.android.support:design:28.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.7.2")
    implementation("com.squareup.retrofit2:converter-gson:2.7.2")
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components["release"])
                groupId = "com.github.dora4"
                artifactId = "dora"
                version = "1.1.30"
            }
        }
    }
}
