pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android.tools.build") {
                useModule("com.android.tools.build:gradle:4.1.2")
            }
            if (requested.id.namespace == "org.jetbrains.kotlin") {
                val kotlinVersion = "1.8.10"
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
            }
            if (requested.id.namespace == "com.github.dcendents") {
                useModule("com.github.dcendents:android-maven-gradle-plugin:1.5")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        mavenLocal()
        google()
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

include(":dora-mvvm")
rootProject.name = "dora"
