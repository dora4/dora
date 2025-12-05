plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    namespace = "dora.mvvm"
    compileSdk = 36
    defaultConfig {
        minSdk = 21
        ndk {
            abiFilters.addAll(setOf("x86", "x86_64", "armeabi-v7a", "arm64-v8a"))
        }
        @Suppress("UnstableApiUsage")
        externalNativeBuild {
            cmake {
                arguments.add("-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON")
                // added to improve security of binary #180
                cFlags("-fPIC")
                cppFlags("-fPIC")
            }
        }
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
        sourceCompatibility(JavaVersion.VERSION_17)
        targetCompatibility(JavaVersion.VERSION_17)
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.21")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.android.support:design:28.0.0")

    api("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    api("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")
    api("androidx.activity:activity-ktx:1.7.0")
    api("androidx.fragment:fragment-ktx:1.7.0")

    api("io.reactivex.rxjava2:rxjava:2.2.21")
    api("io.reactivex.rxjava2:rxandroid:2.1.1")

}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components["release"])
                groupId = "com.github.dora4"
                artifactId = "dora"
                version = "1.3.47"
            }
        }
    }
}
