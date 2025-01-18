pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android.tools.build") {
                useModule("com.android.tools.build:gradle:8.1.0")
            }
            if (requested.id.namespace == "org.jetbrains.kotlin") {
                val kotlin_version = "1.8.10"
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
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
