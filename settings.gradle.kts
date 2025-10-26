pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android.tools.build") {
                useModule("com.android.tools.build:gradle:8.1.1")
            }
            if (requested.id.namespace == "org.jetbrains.kotlin") {
                val kotlin_version = "2.2.21"
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
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
